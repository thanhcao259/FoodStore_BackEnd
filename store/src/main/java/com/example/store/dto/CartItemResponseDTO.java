package com.example.store.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private Long cartId;
    private Long productId;
    private int quantity;
    private double totalPrice;
    private ZonedDateTime addedDate;
    private boolean isDeleted;
    private String name;
    private double price;
    private double discount;
    private String urlImage;
}
