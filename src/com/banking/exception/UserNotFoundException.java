package com.banking.exception;

public class UserNotFoundException extends Exception {

    // Constructor that accepts a message
    public UserNotFoundException(String message) {
        super(message);
    }
}
