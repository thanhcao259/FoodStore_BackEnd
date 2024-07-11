package com.example.store.mapper;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.dto.CartItemsDTO;
import com.example.store.entity.CartItem;

import java.util.List;

public interface ICartItemMapper {
    CartItemResponseDTO toResponseDTO(CartItem cartItem);
    List<CartItemResponseDTO> toResponseDTOs(List<CartItem> cartItems);
    CartItemsDTO toDTO(CartItem cartItem);
    List<CartItemsDTO> toDTOs(List<CartItem> cartItems);

}
