package com.banking.exception;

public class InsufficientFundsException extends Exception {

    // Constructor that accepts a message
    public InsufficientFundsException(String message) {
        super(message);
    }
}
