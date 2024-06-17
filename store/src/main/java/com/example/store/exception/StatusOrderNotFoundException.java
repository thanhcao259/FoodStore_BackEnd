package com.example.store.exception;

public class StatusOrderNotFoundException extends RuntimeException {
    public StatusOrderNotFoundException(String mess) {
        super(mess);
    }
}
