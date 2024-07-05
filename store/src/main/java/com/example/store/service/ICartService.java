package com.example.store.service;

import com.example.store.dto.CartItemResponseDTO;

import java.util.List;

public interface ICartService {
    CartItemResponseDTO addProductToCart(Long productId, int quantity, String username);
    boolean removeProductCart(Long productId, String username);
    CartItemResponseDTO updateQuantityProduct(String username,Long productId, int quantity);
    List<CartItemResponseDTO> getAllCartItemsByUsername(String username);
}
