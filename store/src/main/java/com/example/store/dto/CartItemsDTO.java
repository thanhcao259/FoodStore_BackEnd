package com.example.store.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsDTO {
    private Long idProduct;
    private String urlImage;
    private String name;
    private int quantity;
    private double price;
    private double totalPrice;
    private String prodIdentity;
}
