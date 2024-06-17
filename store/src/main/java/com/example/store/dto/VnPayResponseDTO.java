package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VnPayResponseDTO {
    private String vnpAmount;
    private String vnpBankCode;
    private String vnpTransactionNo;
    private String vnpOrderInfo;
    private String vnpSecureHash;
    private String vnpPayDate;
    private String vnpTxnRef;
}
