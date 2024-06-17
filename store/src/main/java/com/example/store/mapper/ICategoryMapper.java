package com.example.store.mapper;

import com.example.store.dto.CategoryDTO;
import com.example.store.entity.Category;

import java.util.List;

public interface ICategoryMapper {
    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO categoryDTO);
    List<CategoryDTO> toDTOs(List<Category> categoryList);
    List<Category> toEntities(List<CategoryDTO> categoryDTOList);
}
