package com.example.store.mapper.impl;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.dto.CartItemsDTO;
import com.example.store.entity.CartItem;
import com.example.store.mapper.ICartItemMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartItemMapperImpl implements ICartItemMapper {
    @Override
    public CartItemResponseDTO toResponseDTO(CartItem cartItem) {
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
        responseDTO.setProductIdentity(cartItem.getProduct().getIdentity());
        responseDTO.setCategory(cartItem.getProduct().getCategory().getName());
        return responseDTO;
    }

    @Override
    public List<CartItemResponseDTO> toResponseDTOs(List<CartItem> cartItemList) {
        List<CartItemResponseDTO> cartItemResponseDTOs = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            cartItemResponseDTOs.add(toResponseDTO(cartItem));
        }
        return cartItemResponseDTOs;
    }

    @Override
    public CartItemsDTO toDTO(CartItem cartItem) {
        CartItemsDTO newDto = new CartItemsDTO();
        newDto.setIdProduct(cartItem.getProduct().getId());
        newDto.setQuantity(cartItem.getQuantity());
        newDto.setName(cartItem.getProduct().getName());
        newDto.setPrice(cartItem.getProduct().getPrice());
        newDto.setTotalPrice(cartItem.getTotalPrice());
        newDto.setUrlImage(cartItem.getProduct().getUrlImage());
        newDto.setProdIdentity(cartItem.getProduct().getIdentity());

        return newDto;
    }

    @Override
    public List<CartItemsDTO> toDTOs(List<CartItem> cartItems) {
        List<CartItemsDTO> dtoList = new ArrayList<>();
        for(CartItem ci : cartItems){
            dtoList.add(toDTO(ci));
        }
        return dtoList;
    }
}
