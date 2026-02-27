package com.example.kata.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kata.account.model.Account;
import com.example.kata.account.model.Transaction;
import com.example.kata.account.model.TransactionType;
import com.example.kata.account.repository.AccountRepository;
import com.example.kata.account.repository.TransactionRepository;
import com.example.kata.account.DTO.AccountResponse;
import com.example.kata.account.DTO.StatementResponse;
import com.example.kata.account.DTO.TransactionResponse;
import com.example.kata.account.exception.AccountNotFoundException;
import com.example.kata.account.exception.InsufficientFundsException;
import com.example.kata.account.exception.InvalidAmountException;

@Service
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepo;

    TransactionRepository transactionRepo;

    public AccountServiceImpl(AccountRepository accountRepo, TransactionRepository transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }


    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountInfo(long accountId) {
        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        AccountResponse response = new AccountResponse(
            account.getAccountId(), 
            account.getUserFullName(), 
            account.getBalance(), 
            account.getAllowedNegativeBalance()
        );
        return response;
    }

    @Override
    @Transactional
    public TransactionResponse deposit(long accountId, BigDecimal amount) {
        // Check data validity
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Deposited amount must be positive (amount was " + amount + ")");
        }


        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        // Build new transaction
        Transaction newTransaction = new Transaction(
            account, 
            LocalDateTime.now(), 
            TransactionType.DEPOSIT, 
            amount, 
            account.getBalance().add(amount)
        );

        //Persist changes
        account.setBalance(newTransaction.getBalanceAfter());
        newTransaction = transactionRepo.save(newTransaction);

        // Build response
        TransactionResponse response = new TransactionResponse(
            newTransaction.getTransactionId(),
            newTransaction.getTimestamp(),
            newTransaction.getType(),
            newTransaction.getAmount(),
            newTransaction.getBalanceAfter()
        );
        return response;
    }


    @Override
    @Transactional
    public TransactionResponse withdraw(long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Withdrawn amount must be positive (amount was " + amount + ")");
        }


        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        // Check for available balance
        if (account.getBalance().subtract(amount).compareTo(
            BigDecimal.valueOf(-account.getAllowedNegativeBalance())) < 0) {
            throw new InsufficientFundsException(account.getBalance().subtract(amount));
        }

        // Build new transaction
        Transaction newTransaction = new Transaction(
            account, 
            LocalDateTime.now(), 
            TransactionType.WITHDRAWAL, 
            amount, 
            account.getBalance().subtract(amount)
        );

        //Persist changes
        account.setBalance(newTransaction.getBalanceAfter());
        newTransaction = transactionRepo.save(newTransaction);

        // Build response
        TransactionResponse response = new TransactionResponse(
            newTransaction.getTransactionId(),
            newTransaction.getTimestamp(),
            newTransaction.getType(),
            newTransaction.getAmount(),
            newTransaction.getBalanceAfter()
        );
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public StatementResponse getFullAccountStatement(long accountId) {

        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        List<Transaction> transactions = transactionRepo.findAllTransactionsForAccount(accountId);

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