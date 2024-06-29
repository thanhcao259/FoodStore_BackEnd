package com.example.store.service;

import com.example.store.dto.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> getCategoryPagingAndSort(int pageNo, int pageSize, String sortBy, String sortDir);
    CategoryDTO createCategory (CategoryDTO categoryDTO, String username);
    CategoryDTO updateCategoryById (String username, Long id, CategoryDTO categoryDTO);
    Boolean deactiveStatus(Long id);
    Boolean activeStatus(Long id);
    List<CategoryDTO> getCateByAdmin();
    List<CategoryDTO> getAllCate();
}
