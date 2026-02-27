package com.example.kata.account.service;

import com.example.kata.account.DTO.AccountResponse;
import com.example.kata.account.DTO.TransactionResponse;
import com.example.kata.account.DTO.StatementResponse;

import java.math.BigDecimal;

public interface AccountService {
    // Basic account info
    AccountResponse getAccountInfo(long accountId);

    // Basic deposit transaction
    TransactionResponse deposit(long accountId, BigDecimal amount);

    // Basic withdrawal transaction
    TransactionResponse withdraw(long accountId, BigDecimal amount);

    // For a full history of all transactions of an account.
    StatementResponse getFullAccountStatement(long accountId);
}