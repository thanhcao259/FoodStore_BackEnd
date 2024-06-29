package com.example.store.dto;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long Id;
    private AddressDTO address;
    private String name;
    private String phone;
    private ZonedDateTime deliveryTime;
    private double totalPrice;
    private String statusOrder;
    private VnPayResponseDTO vnPayResponseDTO;
    private List<CartItemResponseDTO> cartItemResponseDTOs;
    private String identity;


    public void setCartItemResponseDTOs(List<CartItemResponseDTO> cartItemResponseDTOs) {
        this.cartItemResponseDTOs = cartItemResponseDTOs;
    }
}
