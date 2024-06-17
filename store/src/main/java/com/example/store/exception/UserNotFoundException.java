package com.example.store.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String mess) {
        super(mess);
    }
}
