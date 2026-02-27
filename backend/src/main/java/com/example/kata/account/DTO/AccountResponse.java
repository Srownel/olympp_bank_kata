package com.example.kata.account.DTO;

import java.math.BigDecimal;

public record AccountResponse(
    Long accountId,
    String fullName,
    BigDecimal balance,
    int allowedNegativeBalance
) {}
