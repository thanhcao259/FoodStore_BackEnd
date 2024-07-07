package com.example.store.exception;

public class OTPNotFoundException extends RuntimeException {
    public OTPNotFoundException(String mess) {
        super(mess);
    }
}
