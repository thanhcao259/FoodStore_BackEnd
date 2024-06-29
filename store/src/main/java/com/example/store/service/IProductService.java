package com.example.store.service;

import com.example.store.dto.ListProductPageDTO;
import com.example.store.dto.ProductRequestDTO;
import com.example.store.dto.ProductResponseDTO;

import java.util.List;

public interface IProductService {
    ProductResponseDTO getProductById(Long id);
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getAllByAdmin();
    ListProductPageDTO getProductPage(int page, int pageSize, String sortBy, String sortDir, Long cateId);
    ProductResponseDTO createProduct(String username, ProductRequestDTO productRequestDTO);
    ProductResponseDTO updateProductById(String username, Long id, ProductRequestDTO productRequestDTO);
    Boolean deleteProductById(Long id);
    List<ProductResponseDTO> getProductsByCategory(Long cateId);
    List<ProductResponseDTO> searchProduct(String keyword);

    boolean updateStatus(String username, Long proId);
}
