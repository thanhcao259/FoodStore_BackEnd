package com.example.store.mapper;

import com.example.store.dto.OrderPaymentDTO;
import com.example.store.dto.VnPayResponseDTO;
import com.example.store.entity.PayInfo;

public interface VnPayMapper {
    PayInfo toEntity (OrderPaymentDTO orderPaymentDTO);
    VnPayResponseDTO toDTO (PayInfo payInfo);

}
