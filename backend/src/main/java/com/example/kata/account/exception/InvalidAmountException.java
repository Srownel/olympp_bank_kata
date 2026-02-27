package com.example.kata.account.exception;

public class InvalidAmountException extends AccountInteractionAbstractException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
