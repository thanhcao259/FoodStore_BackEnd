package com.example.store.exception;

public class IdentificationNotFoundException extends RuntimeException {
    public IdentificationNotFoundException(String mess) {
        super(mess);
    }
}
