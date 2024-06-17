package com.example.store.service.implement;

import com.example.store.dto.PaymentRequest;
import com.example.store.service.IPaypalService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalServiceImpl implements IPaypalService {
    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.clientSecret}")
    private String clientSecret;

    @Override
    public String createPayment(PaymentRequest paymentRequest) throws PayPalRESTException {
        // Setup Payment
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(paymentRequest.getAmount());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(paymentRequest.getDescription());

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactionList);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/api/paypal/complete-payment");
        redirectUrls.setReturnUrl("http://localhost:8080/api/paypal/cancel-payment");
        payment.setRedirectUrls(redirectUrls);

        // Create a payment and get link approval
        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
        Payment createdPayment = payment.create(apiContext);

        return createdPayment.getLinks().stream()
                .filter(links -> "approval_url".equals(links.getRel().toLowerCase()))
                .findFirst()
                .map(Links::getHref)
                .orElseThrow(()-> new PayPalRESTException("PayPal Payment failed because no approval link found"));
    }

    @Override
    public String completePayment(String paymentId, String payerId) throws PayPalRESTException {

        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
        Payment payment = new Payment();
        payment.setId(payerId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executionPayment = payment.execute(apiContext, paymentExecution);
        return executionPayment.getState();
    }
}
