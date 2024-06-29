package com.example.store.controllers;

import com.example.store.dto.ProductRequestDTO;
import com.example.store.dto.ProductResponseDTO;
import com.example.store.exception.CategoryNotFoundException;
import com.example.store.exception.ProductNotFoundException;
import com.example.store.service.IProductService;
import com.example.store.service.IUploadFileService;
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
public class ProductController {
    private final IProductService productService;
    private final IUploadFileService uploadFileService;

    public ProductController(IProductService productService, IUploadFileService uploadFileService) {
        this.productService = productService;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("/products/search")
    public ResponseEntity<?> searchProduct(String keyword) {
        try {
            return new ResponseEntity<>(productService.searchProduct(keyword), HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/get-all")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductResponseDTO> products = productService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/admin/products/get-all")
    public ResponseEntity<?> getAllByAdmin() {
        try {
            List<ProductResponseDTO> products = productService.getAllByAdmin();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductPage(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "9") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "asc") String sorDir,
                                            @RequestParam(value = "idCategory", defaultValue = "") Long idCategory) {
        try {
            return new ResponseEntity<>(productService.getProductPage(pageNo, pageSize, sortBy, sorDir, idCategory), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/products/searching")
    public ResponseEntity<?> getSearchPage(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "9") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = "asc") String sorDir,
                                            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        try {
            return new ResponseEntity<>(productService.getSearchProductPage(pageNo, pageSize, sortBy, sorDir, keyword), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long proId) {
        try {
            ProductResponseDTO product = productService.getProductById(proId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Not found product by id: " + proId, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/products-by-cate")
    public ResponseEntity<?> getProductByCate(@RequestParam("cateId") Long cateId) {
        try {
            List<ProductResponseDTO> list = productService.getProductsByCategory(cateId);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>("Not found category by id: " + cateId, HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Not found product", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/admin/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(Authentication auth,
                                        @RequestParam("name") String name, @RequestParam("category_id") Long cateId,
                                        @RequestParam("available") int available, @RequestParam(value = "discount", defaultValue = "0") double discount,
                                        @RequestParam("price") double price, @RequestParam("description") String description,
                                        @RequestParam("image") MultipartFile multipartFile) throws IOException {
        try {
            String username = auth.getName();
//            System.out.println("Request param: " + name +", "+ cateId + ", "+ description);
            String imgUrl = "";
            if (multipartFile != null) {
                imgUrl = uploadFileService.uploadFile(multipartFile);
            }
            ProductRequestDTO newProduct = new ProductRequestDTO(name, cateId, available, discount, price, imgUrl, description, true);
            return new ResponseEntity<>(productService.createProduct(username, newProduct), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>("Not found category by id: " + cateId, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/admin/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(Authentication auth, @PathVariable Long id,
                                           @RequestParam("name") String prodName, @RequestParam("category_id") Long cateId,
                                           @RequestParam("available") int available, @RequestParam("discount") double discount,
                                           @RequestParam("price") double price, @RequestParam("description") String description,
                                           @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        try {
//            System.out.println("Request param: " + prodName +", "+ cateId + ", "+ description);
            String username = auth.getName();
            String imgUrl = "";
            if (multipartFile != null) {
                imgUrl = uploadFileService.uploadFile(multipartFile);
            } else {
                ProductResponseDTO responseDTO = productService.getProductById(id);
                imgUrl = responseDTO.getUrlImage();
            }
            ProductRequestDTO newProduct = new ProductRequestDTO(prodName, cateId, available, discount, price, imgUrl, description);

            return new ResponseEntity<>(productService.updateProductById(username, id, newProduct), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>("Not found category by id: " + cateId, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<?> deleteProduct(Authentication auth, @PathVariable Long id) {
        try {
            String username = auth.getName();
            boolean deleteProduct = productService.deleteProductById(id);
            return new ResponseEntity<>(deleteProduct, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Not found product by id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/product-change-status/{id}")
    public ResponseEntity<?> activateProduct(Authentication auth, @PathVariable("id")Long id){
        try{
            String username = auth.getName();
            return new ResponseEntity<>(productService.updateStatus(username, id), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Not found product by id: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
