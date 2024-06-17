package com.example.store.controllers;

import com.example.store.dto.AddCartDTO;
import com.example.store.dto.CartItemResponseDTO;
import com.example.store.service.ICartService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {
    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/get-cart")
    public ResponseEntity<?> getCartItems(Authentication authentication) {
        try {
            String username = authentication.getName();
            return new ResponseEntity<>(cartService.getAllCartItemsByUsername(username), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/cart/add-cart")
    public ResponseEntity<?> addCartItem(Authentication authentication, @RequestBody AddCartDTO addCartDTO) {
        try {
            String username = authentication.getName();
            Long proId = addCartDTO.getIdProduct();
            int quantity = addCartDTO.getQuantity();
            //System.out.println("Response: {}, {}"+proId +" - "+ quantity);
            CartItemResponseDTO cartItemDTO = cartService.addProductToCart(proId, quantity, username);
            return new ResponseEntity<>(cartItemDTO, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/cart/set-quantity/{idProduct}")
    public ResponseEntity<?> updateCartQuantity(Authentication authentication,
                    @PathVariable("idProduct") Long productId,
                    @RequestParam("quantity") int quantity) {
        try {
            String username = authentication.getName();
            CartItemResponseDTO cartItemResponseDTO = cartService.updateQuantityProduct(username, productId, quantity);
            return new ResponseEntity<>(cartItemResponseDTO, HttpStatus.OK);
        }catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/user/cart/delete-items/{proId}")
    public ResponseEntity<?> deleteProduct(Authentication authentication,
                                           @PathVariable("proId")Long proId) {
        try {
            String username = authentication.getName();
            return new ResponseEntity<>(cartService.removeProductCart(proId,username), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
