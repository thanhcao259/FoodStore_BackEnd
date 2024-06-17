package com.example.store.exception;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException(String mess) {
        super(mess);
    }
}
