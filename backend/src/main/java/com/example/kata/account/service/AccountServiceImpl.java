package com.example.kata.account.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.kata.account.model.Account;
import com.example.kata.account.model.Transaction;
import com.example.kata.account.model.TransactionType;
import com.example.kata.account.DTO.AccountResponse;
import com.example.kata.account.DTO.StatementResponse;
import com.example.kata.account.DTO.TransactionResponse;

@Service
public class AccountServiceImpl implements AccountService {

    // Temporary hard stored entity instead of database query
    // TODO service should be stateless and query account information through repo inside methods.
    private Account account = new Account("John Doe", BigDecimal.valueOf(0), 1000);
    private List<Transaction> transactions = new ArrayList<Transaction>();


    @Override
    public AccountResponse getAccountInfo(long accountId) {
        // TODO get account information from repo properly
        AccountResponse response = new AccountResponse(
            account.getAccountId(), 
            account.getUserFullName(), 
            account.getBalance(), 
            account.getAllowedNegativeBalance()
        );
        return response;
    }

    @Override
    public TransactionResponse deposit(long accountId, BigDecimal amount) {
        // TODO check for validity of input data. Maybe todo in the Controller.

        // TODO get and update account from repo properly

        Transaction newTransaction = new Transaction(
            account, 
            null, 
            TransactionType.DEPOSIT, 
            amount, 
            account.getBalance().add(amount)
        );

        account.setBalance(newTransaction.getBalanceAfter());

        // TODO save transaction through repo properly
        transactions.add(newTransaction);
        TransactionResponse response = new TransactionResponse(
            newTransaction.getTransactionId(),
            null,
            newTransaction.getType(),
            newTransaction.getAmount(),
            newTransaction.getBalanceAfter()
        );
        return response;
    }

    @Override
    public TransactionResponse withdraw(long accountId, BigDecimal amount) throws Exception {
        // TODO check for validity of input data. Maybe todo in the Controller.

        // TODO get and update account from repo properly

        if (account.getBalance().subtract(amount).compareTo(
            BigDecimal.valueOf(-account.getAllowedNegativeBalance())) < 0) {
            throw new Exception("TODO, proper exception handling");
        }

        Transaction newTransaction = new Transaction(
            account, 
            null, 
            TransactionType.WITHDRAWAL, 
            amount, 
            account.getBalance().subtract(amount)
        );

        account.setBalance(newTransaction.getBalanceAfter());

        // TODO save transaction through repo properly
        transactions.add(newTransaction);
        TransactionResponse response = new TransactionResponse(
            newTransaction.getTransactionId(),
            null,
            newTransaction.getType(),
            newTransaction.getAmount(),
            newTransaction.getBalanceAfter()
        );
        return response;
    }

    @Override
    public StatementResponse getFullAccountStatement(long accountId) {
        // TODO get data from repo properly
        List<TransactionResponse> transactionResponses = transactions.stream()
            .map(t -> new TransactionResponse(
                t.getTransactionId(),
                t.getTimestamp(),
                t.getType(),
                t.getAmount(),
                t.getBalanceAfter()
            ))
            .collect(Collectors.toList());

        StatementResponse response = new StatementResponse(
            account.getAccountId(),
            account.getBalance(),
            transactionResponses
        );
        return response;
    }
}