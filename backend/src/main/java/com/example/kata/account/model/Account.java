package com.example.kata.account.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String userFullName;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    private int allowedNegativeBalance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Transaction> transactions = new ArrayList<>();


    protected Account() {};

    public Account(String fullName, BigDecimal initialBalance, int allowedNegativeBalance) {
        this.userFullName = fullName;
        this.balance = initialBalance;
        this.allowedNegativeBalance = allowedNegativeBalance;
    }


    //* GETTERS *//

    public Long getAccountId() { 
        return accountId; 
    }

    public String getUserFullName() { 
        return userFullName; 
    }

    public BigDecimal getBalance() { 
        return balance; 
    }

    public int getAllowedNegativeBalance() { 
        return allowedNegativeBalance;
    }


    //* SETTERS *//

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}