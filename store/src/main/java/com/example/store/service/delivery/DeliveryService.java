package com.example.store.service.delivery;

import com.example.store.entity.delivery.GHNProvince;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeliveryService {

    List<GHNProvince> getProvinces();
}
