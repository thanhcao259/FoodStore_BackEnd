package com.example.store.dto.delivery;

import com.example.store.entity.delivery.GHNProvince;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GHNProvinceResponse {
    private int code;
    private String mess;
    private List<GHNProvince> data;

}
