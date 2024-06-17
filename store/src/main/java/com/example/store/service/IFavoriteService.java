package com.example.store.service;

import com.example.store.dto.ProductResponseDTO;

import java.util.List;

public interface IFavoriteService {
    ProductResponseDTO addFavorite(String username, Long productId);
    boolean removeFavorite(String username, Long productId);
    List<ProductResponseDTO> getListFavorite(String username);
}
