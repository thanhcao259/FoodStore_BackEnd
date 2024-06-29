package com.example.store.controllers;

import com.example.store.dto.CategoryDTO;
import com.example.store.exception.CategoryNotFoundException;
import com.example.store.service.ICategoryService;
import com.example.store.service.IUploadFileService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:3000/")
public class CategoryController {
    private final ICategoryService categoryService;
    private final IUploadFileService uploadFileService;

    public CategoryController(ICategoryService categoryService, IUploadFileService uploadFileService) {
        this.categoryService = categoryService;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories-sort")
    public ResponseEntity<?> getCategoriesPagingAndSorting(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        try {
            List<CategoryDTO> dtoList = categoryService.getCategoryPagingAndSort(pageNo, pageSize, sortBy, sortDir);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        try {
            List<CategoryDTO> dtoList = categoryService.getAllCate();
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/admin/categories")
    public ResponseEntity<?> getCategoriesByAdmin() {
        try {
            List<CategoryDTO> dtoList = categoryService.getCateByAdmin();
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/admin/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCategory(Authentication auth,
                                            @RequestParam(value = "image", required = true) MultipartFile multipartFile,
                                            @RequestParam("name") String name,
                                            @RequestParam("description") String description
    ) throws IOException {
        try {
            String username = auth.getName();
            String imgUrl = uploadFileService.uploadFile(multipartFile);
            CategoryDTO categoryDTO = new CategoryDTO(name, description, imgUrl, true);

            return new ResponseEntity<>(categoryService.createCategory(categoryDTO, username), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.REQUEST_TIMEOUT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/admin/category/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCategory(Authentication auth,
                                            @PathVariable("id") Long cateId,
                                            @RequestParam(value = "image", required = false) MultipartFile multipartFile,
                                            @RequestParam("name") String name,
                                            @RequestParam(value = "description") String description) throws IOException {
        String username = auth.getName();
        CategoryDTO dto = categoryService.getCategoryById(cateId);
        String imgUrl = "";
        if (multipartFile != null) {
            imgUrl = uploadFileService.uploadFile(multipartFile);
        } else {
            imgUrl = dto.getUrlImage();
        }
        CategoryDTO categoryDTO = new CategoryDTO(name, description, imgUrl, true);
        try {
            return new ResponseEntity<>(categoryService.updateCategoryById(username, cateId, categoryDTO), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(value = "id") Long cateId) {
        try {
            return new ResponseEntity<>(categoryService.deactiveStatus(cateId), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/category-activate/{id}")
    public ResponseEntity<?> activateCategory(@PathVariable(value = "id") Long cateId) {
        try {
            return new ResponseEntity<>(categoryService.activeStatus(cateId), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/category-deactivate/{id}")
    public ResponseEntity<?> deactivateCategory(@PathVariable(value = "id") Long cateId) {
        try {
            return new ResponseEntity<>(categoryService.deactiveStatus(cateId), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
