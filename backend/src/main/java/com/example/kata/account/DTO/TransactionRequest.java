package com.example.kata.account.DTO;

import java.math.BigDecimal;


public class TransactionRequest {

    private BigDecimal amount;

    public TransactionRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() { 
        return amount; 
    }
}