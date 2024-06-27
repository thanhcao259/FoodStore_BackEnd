package com.example.store.mapper;

import com.example.store.dto.OrderPaymentDTO;
import com.example.store.dto.VnPayResponseDTO;
import com.example.store.entity.VnPayInfo;

public interface VnPayMapper {
    VnPayInfo toEntity (OrderPaymentDTO orderPaymentDTO);
    VnPayResponseDTO toDTO (VnPayInfo vnPayInfo);

}
