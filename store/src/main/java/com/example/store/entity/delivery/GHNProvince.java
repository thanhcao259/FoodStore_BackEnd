package com.example.store.entity.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GHNProvince {
    @JsonProperty("ProvinceID")
    private int ProvinceID;

    @JsonProperty("ProvinceName")
    private String ProvinceName;

}
