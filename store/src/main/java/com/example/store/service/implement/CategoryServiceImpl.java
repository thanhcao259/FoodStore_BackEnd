package com.example.store.service.implement;

import com.example.store.dto.CategoryDTO;
import com.example.store.entity.Category;
import com.example.store.entity.User;
import com.example.store.exception.CategoryNotFoundException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.mapper.ICategoryMapper;
import com.example.store.repository.ICategoryRepository;
import com.example.store.repository.IProductRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.ICategoryService;
import com.example.store.util.PaginationAndSortingUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private final IUserRepository userRepository;
    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    public CategoryServiceImpl(IUserRepository userRepository, ICategoryRepository categoryRepository, ICategoryMapper categoryMapper) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    @Override
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new CategoryNotFoundException("Not found cate: "+id);
        }
        return categoryMapper.toDTO(category.get());
    }

    @Transactional
    @Override
    public List<CategoryDTO> getCategoryPagingAndSort(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PaginationAndSortingUtils.getPageable(pageNo, pageSize, sortBy, sortDir);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();
        return categoryMapper.toDTOs(categories);
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO, String username) {
        String cateName = categoryDTO.getName();
        String identity = generateIdentity(cateName);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UserNotFoundException("Not found user: "+username);
        }
        Category category = categoryMapper.toEntity(categoryDTO);
        category.setUserCreated(user.get());
        category.setUserUpdated(user.get());
        category.setCreatedDate(ZonedDateTime.now());
        category.setUpdatedDate(ZonedDateTime.now());
        category.setIdentity(identity);
        category.setStatus(true);
        categoryRepository.save(category);
        return categoryDTO;
    }

    @Transactional
    @Override
    public CategoryDTO updateCategoryById(String username, Long id, CategoryDTO categoryDTO) {
        String cateName = categoryDTO.getName();
//        String identity = generateIdentity(cateName);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UserNotFoundException("Not found user: "+username);
        } Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Not found category: "+id);
        }
        Category category = optionalCategory.get();
        category.setUpdatedDate(ZonedDateTime.now());
        category.setUserUpdated(user.get());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setImage(categoryDTO.getUrlImage());
        category.setIdentity(categoryDTO.getIdentity());
//        if(!categoryDTO.getUrlImage().isEmpty()){
//            category.setImage(categoryDTO.getUrlImage());
//        }
        categoryRepository.save(category);
        return categoryDTO;
    }

    @Transactional
    @Override
    public Boolean deactiveStatus(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndStatus(id, true);
        if(optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Not found cate: "+id);
        } Category category = optionalCategory.get();
        category.setStatus(false);
        categoryRepository.save(category);
//        categoryRepository.deleteById(id);
        return true;
    }

    @Transactional
    @Override
    public Boolean activeStatus(Long id){
        Optional<Category> optional = categoryRepository.findById(id);
        if(optional.isEmpty()) {
            throw new CategoryNotFoundException("Not found category");
        } Category category = optional.get();
        category.setStatus(true);
        categoryRepository.save(category);
        return true;
    }

    @Transactional
    @Override
    public List<CategoryDTO> getCateByAdmin() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoriesDTOs = categoryMapper.toDTOs(categories);
        return categoriesDTOs;
    }

    @Override
    public List<CategoryDTO> getAllCate() {
        List<Category> categories = categoryRepository.findAllByStatusIsTrue();
        List<CategoryDTO> categoriesDTOs = categoryMapper.toDTOs(categories);
        return categoriesDTOs;
    }

    private String generateIdentity(String myStr){
        String rs = "";
        String regex = " ";
        myStr = myStr.replaceAll("\\s+", " ");

        for(String s : myStr.split(regex)){
            rs += s.charAt(0);
        } return rs.toUpperCase();
    }
}
