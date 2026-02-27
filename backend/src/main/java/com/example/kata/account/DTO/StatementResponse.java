package com.example.kata.account.DTO;

import java.util.List;
import java.math.BigDecimal;

public record StatementResponse(
    Long accountId,
    BigDecimal currentBalance,
    List<TransactionResponse> transactions
) {}