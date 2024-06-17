package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentDTO {
    private Long addressId;
    private String name;
    private String phone;
    private String vnpAmount;
    private String vnpBankCode;
    private String vnpTransactionNo;
    private String vnpOrderInfo;
    private String vnpSecureHash;
    private String vnpPayDate;
    private String vnpTxnRef;
}
