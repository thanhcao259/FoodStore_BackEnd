package com.example.store.service.implement;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.entity.Cart;
import com.example.store.entity.CartItem;
import com.example.store.entity.Product;
import com.example.store.entity.User;
import com.example.store.exception.CartItemNotFoundException;
import com.example.store.exception.ProductNotFoundException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.exception.VariantProductNotFoundException;
import com.example.store.mapper.ICartItemMapper;
import com.example.store.repository.ICartItemRepository;
import com.example.store.repository.ICartRepository;
import com.example.store.repository.IProductRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.ICartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final ICartItemRepository cartItemRepository;
    private final ICartItemMapper cartItemMapper;

    public CartServiceImpl(ICartRepository cartRepository, IUserRepository userRepository, IProductRepository productRepository, ICartItemRepository cartItemRepository, ICartItemMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
    }

    @Transactional
    @Override
    public CartItemResponseDTO addProductToCart(Long productId, int quantity, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        }
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()) {
            throw new VariantProductNotFoundException("Not found " + productId);
        } Product p = product.get();
        double amount = (1 - p.getDiscount()) * p.getPrice();
        Cart cart = user.get().getCart();
        Optional<CartItem> cartItem = cartItemRepository.findByCartIdAndProductAndIsDeleted(cart.getId(), productId);
        if (cartItem.isEmpty()) {
            CartItem cartItemNew = new CartItem();
            cartItemNew.setCart(cart);
            cartItemNew.setProduct(product.get());
            cartItemNew.setQuantity(quantity);
            cartItemNew.setAddedDate(ZonedDateTime.now());
            cartItemNew.setTotalPrice( quantity * amount);
            CartItem cartItemSaved = cartItemRepository.save(cartItemNew);
            System.out.println("CartItem saved: " + cartItemSaved);
            return cartItemMapper.toResponseDTOs(cartItemSaved);
        } // else
        CartItem cartItemUpdated = cartItem.get();
        int newQuantity = quantity + cartItemUpdated.getQuantity();
        cartItemUpdated.setQuantity(newQuantity);
        cartItemUpdated.setTotalPrice(newQuantity * amount );
        CartItem cartItemSaved = cartItemRepository.save(cartItemUpdated);
        System.out.println("CartItem saved: " + cartItemSaved);
        return cartItemMapper.toResponseDTOs(cartItemSaved);
    }

    @Transactional
    @Override
    public boolean removeProductCart(Long productId, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        } Cart cart = user.get().getCart();
        Optional<CartItem> cartItem = cartItemRepository.findByCartIdAndProductAndIsDeleted(cart.getId(), productId);
        if(cartItem.isEmpty()) {
            throw new CartItemNotFoundException("Not found " + cartItem);
        }
        cartItemRepository.delete(cartItem.get());
        return true;
    }

    @Transactional
    @Override
    public CartItemResponseDTO updateQuantityProduct(String username, Long productId, int quantity) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        } Cart cart = user.get().getCart();
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Not found " + productId);
        } Optional<CartItem> cartItem = cartItemRepository.findByCartIdAndProductAndIsDeleted(cart.getId(), productId);
        if (cartItem.isEmpty()) {
            throw new CartItemNotFoundException("Not found " + cartItem);
        } Product p = product.get();
        double amount = (1 - p.getDiscount()) * p.getPrice();
        CartItem existedCartItem = cartItem.get();
        existedCartItem.setQuantity(quantity);
        existedCartItem.setTotalPrice(quantity * amount );
        CartItem cartItemSaved = cartItemRepository.save(existedCartItem);
        System.out.println("CartItem saved: " + cartItemSaved);
        return cartItemMapper.toResponseDTOs(cartItemSaved);
    }

    @Transactional
    @Override
    public List<CartItemResponseDTO> getAllCartItemsByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Not found " + username);
        }
        Cart cart = user.get().getCart();
        List<CartItem> cartItemList = cartItemRepository.findAllByCartIdAndIsDeleted(cart.getId());
        List<CartItemResponseDTO> cartItemResponseDTOs = cartItemMapper.toResponseDTOs(cartItemList);
        return cartItemResponseDTOs;
    }

}
