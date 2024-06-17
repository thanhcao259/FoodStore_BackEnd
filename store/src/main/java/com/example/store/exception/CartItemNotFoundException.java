package com.example.store.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String mess) {
        super(mess);
    }
}
