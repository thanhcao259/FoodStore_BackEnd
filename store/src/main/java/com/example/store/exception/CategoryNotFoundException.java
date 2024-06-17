package com.example.store.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String mess) {
        super(mess);
    }
}
