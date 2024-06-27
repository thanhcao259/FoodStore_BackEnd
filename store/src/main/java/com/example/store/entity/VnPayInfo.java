package com.example.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vnPay_info")
public class VnPayInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_amount")
    private String vnpAmount;

    @Column(name = "pay_BankCode")
    private String vnpBankCode;

    @Column(name = "pay_transactionNo")
    private String vnpTransactionNo;

    @Column(name = "pay_orderInfo")
    private String vnpOrderInfo;

    @Column(name = "pay_securityHash", length = -1, unique = true)
    private String vnpSecurityHash;

    @Column(name = "pay_date")
    private String vnpPayDate;

    @Column(name = "pay_TxnRef")
    private String vnpTxnRef;

    @OneToOne(mappedBy = "vnPayInfo", cascade = CascadeType.ALL)
    private Order order;

}
