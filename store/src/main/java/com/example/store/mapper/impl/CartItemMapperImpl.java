package com.example.store.mapper.impl;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.entity.CartItem;
import com.example.store.mapper.ICartItemMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartItemMapperImpl implements ICartItemMapper {
    @Override
    public CartItemResponseDTO toResponseDTOs(CartItem cartItem) {
        CartItemResponseDTO responseDTO = new CartItemResponseDTO();
        responseDTO.setCartId(cartItem.getCart().getId());
        responseDTO.setProductId(cartItem.getProduct().getId());
        responseDTO.setQuantity(cartItem.getQuantity());
        responseDTO.setUrlImage(cartItem.getProduct().getUrlImage());
        responseDTO.setPrice(cartItem.getProduct().getPrice());
        responseDTO.setDiscount(cartItem.getProduct().getDiscount());
        responseDTO.setAddedDate(cartItem.getAddedDate());
        responseDTO.setTotalPrice(cartItem.getTotalPrice());
        responseDTO.setDeleted(cartItem.isDeleted());
        responseDTO.setName(cartItem.getProduct().getName());
        return responseDTO;
    }

    @Override
    public List<CartItemResponseDTO> toResponseDTOs(List<CartItem> cartItemList) {
        List<CartItemResponseDTO> cartItemResponseDTOs = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            cartItemResponseDTOs.add(toResponseDTOs(cartItem));
        }
        return cartItemResponseDTOs;
    }
}
