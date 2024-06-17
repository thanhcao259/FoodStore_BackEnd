package com.example.store.mapper;

import com.example.store.dto.OrderResponseDTO;
import com.example.store.dto.ProductRequestDTO;
import com.example.store.dto.ProductResponseDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import java.util.List;

public interface IProductMapper {
    Product toEntity (ProductRequestDTO productRequestDTO);
    ProductResponseDTO toResponseDTO (Product product);
    List<ProductResponseDTO> toResponseDTOs (List<Product> products);

}
