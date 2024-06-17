package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.ZonedDateTime;
import java.util.List;

@Data
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

    private UserDTO userCreated;
    private UserDTO userUpdated;
    private ZonedDateTime createdDate;
    private ZonedDateTime updatedDate;
    private String description;
    @Value("0")
    private Double rate;
}
