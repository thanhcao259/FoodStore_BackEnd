package com.example.store.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String mess) {
        super(mess);
    }
}
