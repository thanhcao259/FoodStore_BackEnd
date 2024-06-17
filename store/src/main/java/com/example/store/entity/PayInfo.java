package com.example.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class PayInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_amount")
    private String payAmount;

    @Column(name = "pay_BankCode")
    private String payBankCode;

    @Column(name = "pay_transactionNo")
    private String payTransactionNo;

    @Column(name = "pay_orderInfo")
    private String payOrderInfo;

    @Column(name = "pay_securityHash", length = -1, unique = true)
    private String paySecurityHash;

    @Column(name = "pay_date")
    private String payDate;

    @Column(name = "pay_TxnRef")
    private String payTxnRef;

}
