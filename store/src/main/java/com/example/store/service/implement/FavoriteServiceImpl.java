package com.example.store.service.implement;

import com.example.store.dto.ProductResponseDTO;
import com.example.store.entity.Favorite;
import com.example.store.entity.Product;
import com.example.store.entity.User;
import com.example.store.exception.ProductNotFoundException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.mapper.IProductMapper;
import com.example.store.repository.IFavoriteRepository;
import com.example.store.repository.IProductRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.IFavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements IFavoriteService {
    private final IFavoriteRepository favoriteRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;
    private final IProductMapper productMapper;

    public FavoriteServiceImpl(IFavoriteRepository favoriteRepository, IProductRepository productRepository, IUserRepository userRepository, IProductMapper productMapper) {
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    @Override
    public ProductResponseDTO addFavorite(String username, Long productId) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if(existingUser.isEmpty()){
            throw new UserNotFoundException("Not found user: "+username);
        } Optional<Product> existingProduct = productRepository.findById(productId);
        if(existingProduct.isEmpty()){
            throw new ProductNotFoundException("Not found product: "+productId);
        } Optional<Favorite> existingFavorite = favoriteRepository.findById(existingProduct.get().getId());
        if(existingFavorite.isPresent()){
            throw new ProductNotFoundException("This favorite is existed!");
        }
        User user = existingUser.get();
        Product product = existingProduct.get();
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
        return productMapper.toResponseDTO(product);
    }

    @Transactional
    @Override
    public boolean removeFavorite(String username, Long productId) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if(existingUser.isEmpty()){
            throw new UserNotFoundException("Not found user: "+username);
        } User user = existingUser.get();
        Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndProductId(user.getId(),productId);
        if(existingFavorite.isEmpty()){
            throw new ProductNotFoundException("Not found product: "+productId);
        } Favorite favorite = existingFavorite.get();
        favoriteRepository.delete(favorite);
        return true;
    }

    @Transactional
    @Override
    public List<ProductResponseDTO> getListFavorite(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if(existingUser.isEmpty()){
            throw new UserNotFoundException("Not found user: "+username);
        } User user = existingUser.get();
        List<Favorite> list = user.getFavorites();
        List<Product> productList = new ArrayList<>();
        for(Favorite item : list){
            productList.add(item.getProduct());
        }
        return productMapper.toResponseDTOs(productList);
    }
}
