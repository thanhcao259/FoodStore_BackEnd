package com.example.store.mapper.impl;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.dto.CategoryDTO;
import com.example.store.entity.CartItem;
import com.example.store.entity.Category;
import com.example.store.mapper.ICartItemMapper;
import com.example.store.mapper.ICategoryMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapperImpl implements ICategoryMapper {
    @Override
    public CategoryDTO toDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setUrlImage(category.getImage());
        return categoryDTO;
    }

    @Override
    public Category toEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setImage(categoryDTO.getUrlImage());
        return category;
    }

    @Override
    public List<CategoryDTO> toDTOs(List<Category> categoryList) {
        List<CategoryDTO> list = new ArrayList<>();
        for (Category category : categoryList) {
            list.add(toDTO(category));
        }
        return list;
    }

    @Override
    public List<Category> toEntities(List<CategoryDTO> categoryDTOList) {
        return List.of();
    }
}
