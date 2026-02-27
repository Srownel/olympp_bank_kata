package com.example.kata.account.exception;

public abstract class AccountInteractionAbstractException extends RuntimeException {

    public AccountInteractionAbstractException(String message) {
        super(message);
    }
    
}