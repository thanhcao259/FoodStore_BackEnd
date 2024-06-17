package com.example.store.exception;

public class UserNameExistedException extends RuntimeException {
    public UserNameExistedException(String mess) {
        super(mess);
    }
}
