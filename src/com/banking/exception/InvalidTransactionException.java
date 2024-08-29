package com.banking.exception;

public class InvalidTransactionException extends Exception {

    // Constructor that accepts a message
    public InvalidTransactionException(String message) {
        super(message);
    }
}
