package com.example.store.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private Long category_id;
    private int available;
    private double discount;
    private double price;
    private List<ReviewResponseDTO> reviews;
    private String urlImage;

    private String description;
    @Value("0")
    private Double rate;
    private String identity;
    private boolean status;
}
