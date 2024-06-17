package com.example.store.exception;

public class VariantProductNotFoundException extends RuntimeException {
    public VariantProductNotFoundException(String mess) {
        super(mess);
    }
}
