package com.example.store.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String mess) {
        super(mess);
    }
}
