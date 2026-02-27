package com.example.kata.account.DTO;

import com.example.kata.account.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record TransactionResponse(
    Long transactionId,
    LocalDateTime timestamp,
    TransactionType type,
    BigDecimal amount,
    BigDecimal balanceAfter
) {}