package com.example.store.service;

import com.example.store.dto.PaymentRequest;
import com.paypal.base.rest.PayPalRESTException;

public interface IPaypalService {
    String  createPayment (PaymentRequest paymentRequest) throws PayPalRESTException;
    String completePayment(String paymentId, String payerId) throws PayPalRESTException;

}
