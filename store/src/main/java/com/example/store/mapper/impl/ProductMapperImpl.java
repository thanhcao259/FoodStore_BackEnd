package com.example.store.mapper.impl;

import com.example.store.dto.ProductRequestDTO;
import com.example.store.dto.ProductResponseDTO;
import com.example.store.entity.Product;
import com.example.store.entity.Reviews;
import com.example.store.mapper.IProductMapper;
import com.example.store.mapper.IReviewMapper;
import com.example.store.mapper.IUserMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapperImpl implements IProductMapper {
    private final IUserMapper userMapper;
    private final IReviewMapper reviewMapper;

    public ProductMapperImpl(IUserMapper userMapper, IReviewMapper reviewMapper) {
        this.userMapper = userMapper;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Product toEntity(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setUrlImage(productRequestDTO.getUrlImage());
        product.setDiscount(productRequestDTO.getDiscount());
        product.setAvailable(productRequestDTO.getAvailable());
        product.setStatus(productRequestDTO.isStatus());
        return product;
    }

    @Override
    public ProductResponseDTO toResponseDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName(product.getName());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setUrlImage(product.getUrlImage());
        productResponseDTO.setDiscount(product.getDiscount());
        productResponseDTO.setAvailable(product.getAvailable());
        productResponseDTO.setId(product.getId());
        productResponseDTO.setCategory_id(product.getCategory().getId());
        productResponseDTO.setIdentity(product.getIdentity());
        productResponseDTO.setStatus(product.isStatus());
        productResponseDTO.setUpdatedDate(product.getUpdatedDate());

        // calculate avg rate of each product
        double countRate = 0;
        if(product.getReviews() != null){
            productResponseDTO.setReviews(reviewMapper.toDTOs(product.getReviews()));
            for(Reviews item : product.getReviews()){
                countRate += item.getRate();
            } countRate = countRate / product.getReviews().size();
        } productResponseDTO.setRate(countRate);
        return productResponseDTO;
    }

    @Override
    public List<ProductResponseDTO> toResponseDTOs(List<Product> products) {
        List<ProductResponseDTO> productResponseDTOs = new ArrayList<>();
        for(Product product : products){
            productResponseDTOs.add(toResponseDTO(product));
        }
        return productResponseDTOs;
    }
}
