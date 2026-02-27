package com.example.kata.account.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends AccountInteractionAbstractException {

    public InsufficientFundsException(BigDecimal projectedBalance) {
        super("Withdrawal amount exceeds available balance (projected balance would have been " + projectedBalance + ")");
    }

}