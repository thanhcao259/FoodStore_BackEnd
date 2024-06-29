package com.example.store.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    private Long id;
    private String name;
    private Long category_id;
    private int available;
    private double discount;
    private double price;
    private String urlImage;
    private String description;
    private String identity;

    public ProductRequestDTO(String name, Long category_id, int available, double discount, double price, String urlImage, String description) {
        this.name = name;
        this.category_id = category_id;
        this.available = available;
        this.discount = discount;
        this.price = price;
        this.urlImage = urlImage;
        this.description = description;
    }

}
