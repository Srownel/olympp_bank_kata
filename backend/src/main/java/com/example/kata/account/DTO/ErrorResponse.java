package com.example.kata.account.DTO;

public record ErrorResponse(
    String error,
    String message
) {}