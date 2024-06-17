package com.example.store.mapper.impl;

import com.example.store.dto.OrderPaymentDTO;
import com.example.store.dto.VnPayResponseDTO;
import com.example.store.entity.PayInfo;
import com.example.store.mapper.VnPayMapper;
import org.springframework.stereotype.Component;

@Component
public class VnPayMapperImpl implements VnPayMapper {
    @Override
    public PayInfo toEntity(OrderPaymentDTO orderPaymentDTO) {
        PayInfo payInfo = new PayInfo();
        payInfo.setPayAmount(orderPaymentDTO.getVnpAmount());
        payInfo.setPayBankCode(orderPaymentDTO.getVnpBankCode());
        payInfo.setPayTransactionNo(orderPaymentDTO.getVnpTransactionNo());
        payInfo.setPaySecurityHash(orderPaymentDTO.getVnpSecureHash());
        payInfo.setPayDate(orderPaymentDTO.getVnpPayDate());
        payInfo.setPayTxnRef(orderPaymentDTO.getVnpTxnRef());
        payInfo.setPayOrderInfo(orderPaymentDTO.getVnpOrderInfo());
        return payInfo;
    }

    @Override
    public VnPayResponseDTO toDTO(PayInfo payInfo) {
        VnPayResponseDTO vnPayResponseDTO = new VnPayResponseDTO();
        vnPayResponseDTO.setVnpBankCode(payInfo.getPayBankCode());
        vnPayResponseDTO.setVnpTransactionNo(payInfo.getPayTransactionNo());
        vnPayResponseDTO.setVnpSecureHash(payInfo.getPaySecurityHash());
        vnPayResponseDTO.setVnpPayDate(payInfo.getPayDate());
        vnPayResponseDTO.setVnpOrderInfo(payInfo.getPayOrderInfo());
        vnPayResponseDTO.setVnpAmount(payInfo.getPayAmount());
        return vnPayResponseDTO;
    }
}
