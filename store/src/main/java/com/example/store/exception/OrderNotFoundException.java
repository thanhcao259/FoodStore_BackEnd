package com.example.store.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String mess) {
        super(mess);
    }
}
