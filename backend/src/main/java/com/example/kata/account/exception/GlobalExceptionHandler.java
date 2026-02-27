package com.example.kata.account.exception;

import com.example.kata.account.DTO.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// TODO : Add a general logger system to store full error stacktraces for debugging.


@RestControllerAdvice
public class GlobalExceptionHandler {

    // Meant to be thrown when accessing an accountId that doesn't exist
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
            "ACCOUNT_NOT_FOUND",
            exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Meant to be thrown when withdrawing unavailable funds
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
            "INSUFFICIENT_FUNDS",
            exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Meant to be thrown when input values are invalid (deposit negative amount for example)
    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAmount(InvalidAmountException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
            "INVALID_AMOUNT",
            exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Not meant to be thrown, it means we have forgotten to handle some (custom) exception types
    @ExceptionHandler(AccountInteractionAbstractException.class)
    public ResponseEntity<ErrorResponse> handleGenericAccountInteractionException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_ERROR",
            "An unexpected process error occurred. Please contact the technical department."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
