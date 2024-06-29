package com.example.store.service.implement;

import com.example.store.dto.ListProductPageDTO;
import com.example.store.dto.ProductRequestDTO;
import com.example.store.dto.ProductResponseDTO;
import com.example.store.entity.Category;
import com.example.store.entity.Product;
import com.example.store.entity.User;
import com.example.store.exception.CategoryNotFoundException;
import com.example.store.exception.ProductNotFoundException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.mapper.IProductMapper;
import com.example.store.repository.ICategoryRepository;
import com.example.store.repository.IProductRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.IProductService;
import com.example.store.service.IUserService;
import com.example.store.util.PaginationAndSortingUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final IUserRepository userRepository;
    private final IProductMapper productMapper;
    private final ICategoryRepository categoryRepository;

    public ProductServiceImpl(IProductRepository productRepository, IUserRepository userRepository, IProductMapper productMapper, ICategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public ProductResponseDTO getProductById(Long id) {
        Optional<Product> existedProduct = productRepository.findById(id);
        if (existedProduct.isEmpty()) {
            throw new ProductNotFoundException("Not found product with id: " + id);
        }
        ProductResponseDTO dto = productMapper.toResponseDTO(existedProduct.get());
        return dto;
    }

    @Transactional
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAllByStatus(true);
        List<ProductResponseDTO> dtoList = productMapper.toResponseDTOs(products);
        return dtoList;
    }

    @Override
    public List<ProductResponseDTO> getAllByAdmin() {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDTO> dtoList = productMapper.toResponseDTOs(products);
        return dtoList;
    }

    @Transactional
    @Override
    public ListProductPageDTO getProductPage(int pageNo, int pageSize, String sortBy, String sortDir, Long cateId) {
        Pageable pageable = PaginationAndSortingUtils.getPageable(pageNo, pageSize, sortBy, sortDir);
        // Not found cate, show all of product
        if (cateId == null) {
            List<Product> products = productRepository.findAllByStatus(true);
            int pageSizes = (int) Math.ceil((double) products.size() / pageSize);
            Page<Product> productsPageable = productRepository.findAll(pageable);
            List<Product> productsContent = productsPageable.getContent();
            return new ListProductPageDTO(productMapper.toResponseDTOs(productsContent), pageSizes);
        }
        List<Product> productByCate = productRepository.findByCategoryIdAndStatus(cateId, true);
        int pageSizes = (int) Math.ceil((double) productByCate.size() / pageSize);
        Page<Product> productPage = productRepository.findByCategoryId(cateId, pageable);
        List<Product> productsContent = productPage.getContent();
        return new ListProductPageDTO(productMapper.toResponseDTOs(productsContent), pageSizes);
    }

    @Override
    public ListProductPageDTO getSearchProductPage(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        Pageable pageable = PaginationAndSortingUtils.getPageable(page, pageSize, sortBy, sortDir);

        List<Product> products = productRepository.findByNameAndStatus(keyword, true);
        int pageSizes = (int) (Math.ceil((double) products.size()/pageSize));
        Page<Product> productPage = productRepository.findByNameAndStatusTrue(keyword, pageable);
        List<Product> content = productPage.getContent();
        return new ListProductPageDTO(productMapper.toResponseDTOs(content), pageSizes);
    }

    @Transactional
    @Override
    public ProductResponseDTO createProduct(String username, ProductRequestDTO productRequestDTO) {
        Optional<User> existedUser = userRepository.findByUsername(username);
        if (existedUser.isEmpty()) {
            throw new UserNotFoundException("Not found user with name: " + username);
        }
        User user = existedUser.get();
        Optional<Category> existedCate = categoryRepository.findById(productRequestDTO.getCategory_id());
        if (existedCate.isEmpty()) {
            throw new CategoryNotFoundException("Not found category with id: " + productRequestDTO.getId());
        }
        Category category = existedCate.get();
        Product product = productMapper.toEntity(productRequestDTO);
        category.getProducts().add(product);
        product.setCategory(category);
        product.setUserCreated(user);
        product.setUserUpdated(user);
        product.setCreatedDate(ZonedDateTime.now());
        product.setUpdatedDate(ZonedDateTime.now());
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }

    @Transactional
    @Override
    public ProductResponseDTO updateProductById(String username, Long id, ProductRequestDTO productRequestDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Not found user with name: " + username);
        }
        User user = optionalUser.get();
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Not found product with id: " + id);
        }
        Product product = optionalProduct.get();
        Optional<Category> optionalCate = categoryRepository.findById(productRequestDTO.getCategory_id());
        if (optionalCate.isEmpty()) {
            throw new CategoryNotFoundException("Not found category with id: " + productRequestDTO.getCategory_id());
        }
        Category category = optionalCate.get();
        String cateIdentity = category.getIdentity();
        String productIdentity = generateIdentity(cateIdentity);

        product.setName(productRequestDTO.getName());
        product.setAvailable(productRequestDTO.getAvailable());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setDiscount(productRequestDTO.getDiscount());
        if (productRequestDTO.getUrlImage().isEmpty()) {
            product.setUrlImage(productRequestDTO.getUrlImage());
        }
        product.setUserUpdated(user);
        product.setUpdatedDate(ZonedDateTime.now());
        product.setCategory(category);
        product.setIdentity(productIdentity);

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }

    @Transactional
    @Override
    public Boolean deleteProductById(Long id) {
        Optional<Product> existedProduct = productRepository.findById(id);
        if (existedProduct.isEmpty()) {
            throw new ProductNotFoundException("Not found product with id: " + id);
        }
        Product product = existedProduct.get();
        productRepository.delete(product);
        return true;
    }

    @Transactional
    @Override
    public List<ProductResponseDTO> getProductsByCategory(Long cateId) {
        Optional<Category> existedCate = categoryRepository.findById(cateId);
        if (existedCate.isEmpty()) {
            throw new CategoryNotFoundException("Not found category with id: " + cateId);
        }
        Category category = existedCate.get();
        List<Product> productList = category.getProducts();

        List<ProductResponseDTO> dtoList = productMapper.toResponseDTOs(productList);
        return dtoList;
    }

    @Transactional
    @Override
    public List<ProductResponseDTO> searchProduct(String keyword) {
        List<Product> existedProduct = productRepository.findByNameContainingIgnoreCase(keyword);
        if (existedProduct.isEmpty()) {
            throw new ProductNotFoundException("Not found product with name: " + keyword);
        }
        List<ProductResponseDTO> dtoList = productMapper.toResponseDTOs(existedProduct);
        return dtoList;
    }

    @Override
    public boolean updateStatus(String username, Long proId) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("Not found username");
        } Optional<Product> optionalProduct = productRepository.findById(proId);
        if (optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Not found product");
        } Product product = optionalProduct.get();
        if(product.isStatus()){
            product.setStatus(false);
        } else {
            product.setStatus(true);
        } productRepository.save(product);
        return true;
    }

    private String generateIdentity(String myString) {
        // Generating random doubles
        String strRand = myString+"_"+String.valueOf(Math.round(Math.random() * 10000)); // "myString_1234"

        return strRand.toUpperCase();
    }
}
