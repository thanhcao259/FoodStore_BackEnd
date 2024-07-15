package com.example.store.controllers;

import com.example.store.config.VnPayConfig;
import com.example.store.dto.*;
import com.example.store.entity.CartItem;
import com.example.store.service.EmailService;
import com.example.store.service.IOrderService;
import com.example.store.service.IProductService;
import com.example.store.service.IUserService;
import com.example.store.util.CurrencyUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class VnPayController {
    private static final Logger log = LoggerFactory.getLogger(VnPayController.class);
    private final IOrderService orderService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IUserService userService;
    @Autowired
    private CurrencyUtils currencyUtils;
    @Autowired
    private IProductService productService;

    public VnPayController(IOrderService orderService) {
        this.orderService = orderService;
    }

    HashMap<String, Double> priceHm = new HashMap<>();
    HashMap<String, String> infoCustomer = new HashMap<>();

    @GetMapping("/pay")
    public String payOrder(Authentication auth, @RequestParam("addressId") Long addressId,
                           @RequestParam("name") String name, @RequestParam("phone") String phone,
                           @RequestParam("totalPrice") double totalAmount,
                           @RequestParam("vat") double vat, @RequestParam("feeShip")double feeShip) throws UnsupportedEncodingException {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setAddressId(addressId);
        requestDTO.setName(name);
        requestDTO.setPhone(phone);
        String username = auth.getName();
        UserDTO userDTO = userService.findUserByName(username);
        String email = userDTO.getEmail();
        infoCustomer.put("email", email);

        log.info("response to client: {}, {}, {}", totalAmount, vat, feeShip);
        String orderType = "other";
        long amount = (long) ((totalAmount+vat+feeShip)*100);
        priceHm.put("totalAmount", totalAmount);
        priceHm.put("feeShip", feeShip);
        priceHm.put("vat", vat);


        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
        String vnp_Version = VnPayConfig.vnp_Version;
        String vnp_Command = VnPayConfig.vnp_Command;
        String vnp_BankCode = VnPayConfig.vnp_BankCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));

        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don: "+vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl
                +"?infoUsername="+URLEncoder.encode(username, StandardCharsets.UTF_8)
                +"&infoName="+ URLEncoder.encode(requestDTO.getName(), StandardCharsets.UTF_8)
                +"&infoPhone="+ URLEncoder.encode(requestDTO.getPhone(), StandardCharsets.UTF_8)
                +"&infoAddressId="+requestDTO.getAddressId());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = fmt.format(calendar.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        calendar.add(Calendar.HOUR, 15);
        String vnp_Exp = fmt.format(calendar.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_Exp);

        List fieldsName = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldsName);
        StringBuilder hashData =  new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator it = fieldsName.iterator();
        while (it.hasNext()) {
            String fieldName = (String)it.next();
            String fieldValue = vnp_Params.get(fieldName);
            if((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build hash quey
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if(it.hasNext()){
                    query.append('&'); hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        log.info("query: " + queryUrl);
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?"+queryUrl;
        return paymentUrl;
    }


    @GetMapping("/payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParam, HttpServletResponse response) throws IOException {
        try {
            String username = queryParam.get("infoUsername");
            String vnp_ResponseCode = queryParam.get("vnp_ResponseCode");
            String vnp_Amount = queryParam.get("vnp_Amount");
            String vnp_BankCode = queryParam.get("vnp_BankCode");
            String vnp_TransactionNo = queryParam.get("vnp_TransactionNo");
            String vnp_OrderInfo = queryParam.get("vnp_OrderInfo");
            String vnp_SecureHash = queryParam.get("vnp_SecureHash");
            String vnp_PayDate = queryParam.get("vnp_PayDate");
            String vnp_TxnRef = queryParam.get("vnp_TxnRef");
            String infoName = queryParam.get("infoName");
            String infoPhone = queryParam.get("infoPhone");
            Long infoAddress = Long.valueOf(queryParam.get("infoAddressId"));

            OrderPaymentDTO orderPaymentDTO = new OrderPaymentDTO(infoAddress, infoName, infoPhone,
                    vnp_Amount, vnp_BankCode, vnp_TransactionNo, vnp_OrderInfo, vnp_SecureHash, vnp_PayDate, vnp_TxnRef);

            ///
            double totalAmount = priceHm.get("totalAmount");
            double vat = priceHm.get("vat");
            double feeShip = priceHm.get("feeShip");
            double totalPrice = totalAmount + vat + feeShip;
            ZonedDateTime payDate = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm");
            String strPayDate = fmt.format(payDate);

            String strTotalAmount = currencyUtils.formatCurrency(totalAmount);
            String strVAT = currencyUtils.formatCurrency(vat);
            String strFeeShip = currencyUtils.formatCurrency(feeShip);
            String strTotal = currencyUtils.formatCurrency(totalPrice);

            String email = infoCustomer.get("email");
            String subject = "Thank you for purchasing at FoodShop!";

            if ("00".equals(vnp_ResponseCode)) {
                OrderResponseDTO responseDTO =orderService.orderPayment(username, orderPaymentDTO);
                String identity = responseDTO.getIdentity();
                List<CartItemsDTO> itemsDTOs = productService.getItemByOrder(identity);

                log.info("Check HM: {}, {}, {} = {} via {} order {}",strTotalAmount, strVAT, strFeeShip, strTotal, email, identity);
//                sendEmail(email, identity, totalAmount, feeShip, vat);

                Map<String, Object> model = new HashMap<>();
                model.put("username", username);
                model.put("identity", identity);
                model.put("total", strTotal);
                model.put("totalAmount", strTotalAmount);
                model.put("vat", strVAT);
                model.put("feeShip", strFeeShip);
                model.put("payDate", strPayDate);
                model.put("listItem", itemsDTOs);

                emailService.sendHTMLEmail(email,subject,model);
              response.sendRedirect("https://food-store-front-end.vercel.app/payment/success");
//              response.sendRedirect("http://localhost:3000/payment/success");
            } else {
//                log.info("Payment failed, redirecting to failed page");
                response.sendRedirect("https://food-store-front-end.vercel.app/payment/failed");
                response.sendRedirect("http://localhost:3000/payment/failed");
            }
        } catch (Exception e) {
            log.error("Exception during payment callback processing", e);
            e.printStackTrace();
            response.sendRedirect("https://food-store-front-end.vercel.app/payment/failed");
            response.sendRedirect("http://localhost:3000/payment/failed");
        }
    }

    private void sendEmail(String email,String identity, double totalAmount, double feeShip, double vat){
        double total = totalAmount + vat + feeShip;
        String strTotalAmount = currencyUtils.formatCurrency(totalAmount);
        String strVAT = currencyUtils.formatCurrency(vat);
        String strFeeShip = currencyUtils.formatCurrency(feeShip);
        String strTotal = currencyUtils.formatCurrency(total);
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("Đặt hàng thành công!")
                .text("Cảm ơn bạn đã mua hàng!\n" +
                        "Dưới đây là thông tin đơn hàng của bạn:" +
                        "\nMã hóa đơn: "+identity+
                        "\nTổng tiền sản phẩm: "+strTotalAmount+
                        "\nVAT: "+strVAT+
                        "\nPhí vận chuyển: "+strFeeShip+
                        "\nTổng tiền: "+strTotal
                )
                .build();
        emailService.sendSimpleMessage(mailBody);
    }
}
