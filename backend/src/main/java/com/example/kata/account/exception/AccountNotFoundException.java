package com.example.kata.account.exception;

public class AccountNotFoundException extends AccountInteractionAbstractException {
    private final long accountId;

    public AccountNotFoundException(long accountId) {
        super("Account ID: " + accountId + " does not exist");
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }
}
