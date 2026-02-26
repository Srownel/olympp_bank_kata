package com.example.kata.account.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(precision = 19, scale = 2)
    private BigDecimal balanceAfter;


    protected Transaction() {};

    public Transaction(Account account, LocalDateTime timestamp, TransactionType type, BigDecimal amount, BigDecimal balanceAfter) {
        this.account = account;
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    //* GETTERS *//

    public Long getTransactionId() { 
        return transactionId;
    }

    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }

    public TransactionType getType() { 
        return type; 
    }

    public BigDecimal getAmount() { 
        return amount; 
    }

    public BigDecimal getBalanceAfter() { 
        return balanceAfter;
    }
}