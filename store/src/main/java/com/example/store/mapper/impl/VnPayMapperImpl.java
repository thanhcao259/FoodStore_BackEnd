package com.example.store.mapper.impl;

import com.example.store.dto.OrderPaymentDTO;
import com.example.store.dto.VnPayResponseDTO;
import com.example.store.entity.VnPayInfo;
import com.example.store.mapper.VnPayMapper;
import org.springframework.stereotype.Component;

@Component
public class VnPayMapperImpl implements VnPayMapper {
    @Override
    public VnPayInfo toEntity(OrderPaymentDTO orderPaymentDTO) {
        VnPayInfo vnPayInfo = new VnPayInfo();
        vnPayInfo.setVnpAmount(orderPaymentDTO.getVnpAmount());
        vnPayInfo.setVnpBankCode(orderPaymentDTO.getVnpBankCode());
        vnPayInfo.setVnpTransactionNo(orderPaymentDTO.getVnpTransactionNo());
        vnPayInfo.setVnpSecurityHash(orderPaymentDTO.getVnpSecureHash());
        vnPayInfo.setVnpPayDate(orderPaymentDTO.getVnpPayDate());
        vnPayInfo.setVnpTxnRef(orderPaymentDTO.getVnpTxnRef());
        vnPayInfo.setVnpOrderInfo(orderPaymentDTO.getVnpOrderInfo());
        return vnPayInfo;
    }

    @Override
    public VnPayResponseDTO toDTO(VnPayInfo vnPayInfo) {
        VnPayResponseDTO vnPayResponseDTO = new VnPayResponseDTO();
        vnPayResponseDTO.setVnpBankCode(vnPayInfo.getVnpBankCode());
        vnPayResponseDTO.setVnpTransactionNo(vnPayInfo.getVnpTransactionNo());
        vnPayResponseDTO.setVnpSecureHash(vnPayInfo.getVnpSecurityHash());
        vnPayResponseDTO.setVnpPayDate(vnPayInfo.getVnpPayDate());
        vnPayResponseDTO.setVnpOrderInfo(vnPayInfo.getVnpOrderInfo());
        vnPayResponseDTO.setVnpAmount(vnPayInfo.getVnpAmount());
        vnPayResponseDTO.setVnpTxnRef(vnPayInfo.getVnpTxnRef());
        return vnPayResponseDTO;
    }
}
