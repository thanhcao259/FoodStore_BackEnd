package com.example.store.controllers;

import com.example.store.config.VnPayConfig;
import com.example.store.dto.OrderPaymentDTO;
import com.example.store.dto.OrderRequestDTO;
import com.example.store.service.IOrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class VnPayController {
    private final IOrderService orderService;

    public VnPayController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/pay")
    public String payOrder(Authentication auth, @RequestParam("addressId") Long addressId,
                           @RequestParam("name") String name, @RequestParam("phone") String phone,
                           @RequestParam("totalPrice") double totalPrice) throws UnsupportedEncodingException {
        String username = auth.getName();
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setAddressId(addressId);
        requestDTO.setName(username);
        requestDTO.setPhone(phone);

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = (long) (totalPrice*100);

        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don: "+vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl+"?infoUsername="+username+
                "&infoName="+ URLEncoder.encode(requestDTO.getName())
                +"&infoPhone="+ URLEncoder.encode(requestDTO.getPhone())
                +"&infoAddressId="+requestDTO.getAddressId());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String vnp_CreateDate = fmt.format(calendar.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        calendar.add(Calendar.MINUTE, 15);
        String vnp_Exp = fmt.format(calendar.getTime());
        vnp_Params.put("vnp_Exp", vnp_Exp);

        List fieldsName = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldsName);
        StringBuilder hashData =  new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator it = fieldsName.iterator();
        while (it.hasNext()) {
            String fieldName = (String)it.next();
            String fieldValue = (String)vnp_Params.get(fieldName);
            if((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build hash quey
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if(it.hasNext()){
                    query.append("&"); hashData.append("&");
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return VnPayConfig.vnp_PayUrl+"?"+queryUrl;
    }

    @GetMapping("/payment-callback")
    public void callBack(@RequestParam Map<String, String> queryParam, HttpServletResponse response) throws IOException {
        String username = queryParam.get("infoUsername");
        String vnp_ResponseCode = queryParam.get("vnp_ResponseCode");
        String vnp_Amount = queryParam.get("vnp_Amount");
        String vnp_BankCode = queryParam.get("vnp_BankCode");
        String vnp_TransactionNo = queryParam.get("vnp_TransactionNo");
        String vnp_OrderInfo = queryParam.get("vnp_OrderInfo");
        String vnp_SecureHash = queryParam.get("vnp_SecureHash");
        String vnp_PayDate = queryParam.get("vnp_PayDate");
        String vnp_TnxRef = queryParam.get("vnp_TnxRef");
        String infoName = queryParam.get("infoName");
        String infoPhone = queryParam.get("infoPhone");

        Long infoAddress = Long.parseLong(queryParam.get("infoAddressId"));
        OrderPaymentDTO orderPaymentDTO = new OrderPaymentDTO(infoAddress, infoName, infoPhone, vnp_Amount, vnp_BankCode, vnp_TransactionNo, vnp_OrderInfo, vnp_SecureHash, vnp_PayDate, vnp_TnxRef);
        if("00".equals(vnp_ResponseCode)) {
            orderService.orderPayment(username, orderPaymentDTO);
            response.sendRedirect("http://localhost:3000/payment/success");
        } else {
            response.sendRedirect("http://localhost:3000/payment/failed");
        }
    }
}
