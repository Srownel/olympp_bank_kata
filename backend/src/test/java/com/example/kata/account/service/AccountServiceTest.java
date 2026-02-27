package com.example.kata.account.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.kata.account.DTO.AccountResponse;
import com.example.kata.account.DTO.StatementResponse;
import com.example.kata.account.DTO.TransactionResponse;
import com.example.kata.account.exception.AccountNotFoundException;
import com.example.kata.account.exception.InsufficientFundsException;
import com.example.kata.account.exception.InvalidAmountException;
import com.example.kata.account.model.Account;
import com.example.kata.account.model.Transaction;
import com.example.kata.account.model.TransactionType;
import com.example.kata.account.repository.AccountRepository;
import com.example.kata.account.repository.TransactionRepository;

/* Some unit tests for AccountServiceImpl. Could use some more. */

@ExtendWith(MockitoExtension.class)
@DisplayName("Account Service Tests")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private AccountServiceImpl accountService;
    
    private Account testAccount;
    private static final long TEST_ACCOUNT_ID = 1L;
    private static final String TEST_USER_NAME = "John Do";
    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(500.00);
    private static final int MAXIMUM_NEGATIVE_BALANCE = 1000;

    @BeforeEach
    void setUp() {
        testAccount = new Account(TEST_USER_NAME, INITIAL_BALANCE, MAXIMUM_NEGATIVE_BALANCE);
    }


    // ** ACCOUNT INFO TESTS ** //

    @Test
    @DisplayName("Should return account information for existing account")
    void getExistingAccountInfo() {
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testAccount));

        AccountResponse response = accountService.getAccountInfo(TEST_ACCOUNT_ID);
        assertNotNull(response, "Response should not be null");
        assertEquals(TEST_USER_NAME, response.fullName(), "User name should match");
        assertEquals(INITIAL_BALANCE, response.balance(), "Balance should match");
        assertEquals(MAXIMUM_NEGATIVE_BALANCE, response.allowedNegativeBalance(), "Max negative balance should match");

        verify(accountRepository).findById(TEST_ACCOUNT_ID);
    }

    @Test
    @DisplayName("Should throw exception when account not found")
    void getNonExistingAccountInfo() {
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountInfo(TEST_ACCOUNT_ID);
        }, "Should throw exception when account not found");

        verify(accountRepository).findById(TEST_ACCOUNT_ID);
    }


    // ** DEPOSIT TESTS ** //

    @Test
    @DisplayName("Deposit should return proper Transaction data")
    void depositReturnsTransaction() {
        BigDecimal depositAmount = BigDecimal.valueOf(100.00);
        BigDecimal expectedBalance = INITIAL_BALANCE.add(depositAmount);
        
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionResponse response = accountService.deposit(TEST_ACCOUNT_ID, depositAmount);
        assertNotNull(response, "Response should not be null");
        assertEquals(TransactionType.DEPOSIT, response.type(), "Transaction type should be DEPOSIT");
        assertEquals(depositAmount, response.amount(), "Amount should match deposit");
        assertEquals(expectedBalance, response.balanceAfter(), "Balance should increase by amount");
        
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should reject deposit with negative amount")
    void depositWithNegativeAmount_throwsException() {
        BigDecimal negativeAmount = BigDecimal.valueOf(-50.00);

        assertThrows(InvalidAmountException.class, () -> {
            accountService.deposit(TEST_ACCOUNT_ID, negativeAmount);
        }, "Should throw exception for negative deposit amount");
        
        verifyNoInteractions(accountRepository, transactionRepository);
    }

    @Test
    @DisplayName("Should reject deposit with zero amount")
    void depositWithZeroAmount_throwsException() {
        BigDecimal zeroAmount = BigDecimal.ZERO;

        assertThrows(InvalidAmountException.class, () -> {
            accountService.deposit(TEST_ACCOUNT_ID, zeroAmount);
        }, "Should throw exception for zero deposit amount");

        verifyNoInteractions(accountRepository, transactionRepository);
    }


    // ** WITHDRAWAL TESTS ** //

    @Test
    @DisplayName("Should successfully withdraw with sufficient balance")
    void withdrawReturnsTransaction() throws Exception {
        BigDecimal withdrawAmount = BigDecimal.valueOf(200.00);
        BigDecimal expectedBalance = INITIAL_BALANCE.subtract(withdrawAmount);
        
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionResponse response = accountService.withdraw(TEST_ACCOUNT_ID, withdrawAmount);
        assertNotNull(response, "Response should not be null");
        assertEquals(TransactionType.WITHDRAWAL, response.type(), "Transaction type should be WITHDRAWAL");
        assertEquals(withdrawAmount, response.amount(), "Amount should match withdrawal");
        assertEquals(expectedBalance, response.balanceAfter(), "Balance should decrease by amount");
        
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should allow withdrawal to negative amount within limit")
    void withdrawToNegativeAmountWithinLimit() throws Exception {
        // Account with 500, withdraw 800, goes to -300 (within -1000 limit)
        BigDecimal withdrawAmount = BigDecimal.valueOf(800.00);
        BigDecimal expectedBalance = INITIAL_BALANCE.subtract(withdrawAmount); // -300

        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));


        TransactionResponse response = accountService.withdraw(TEST_ACCOUNT_ID, withdrawAmount);
        assertEquals(expectedBalance, response.balanceAfter(), "Expected balance should match");
        assertTrue(response.balanceAfter().compareTo(BigDecimal.valueOf(-MAXIMUM_NEGATIVE_BALANCE)) >= 0, "Balance should not exceed limit");
    }

    @Test
    @DisplayName("Should reject withdrawal exceeding maximum negative limit")
    void withdrawToNegativeAmountBeyondLimit() {
        // Aaccount with 500, withdraw 1501 -> would go to -1001 (exceeds -1000 limit)
        BigDecimal withdrawAmount = BigDecimal.valueOf(1501.00);

        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testAccount));

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.withdraw(TEST_ACCOUNT_ID, withdrawAmount);
        }, "Should throw exception when exceeding limit");


        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should reject withdrawal with negative amount")
    void withdrawNegativeAmount_throwsException() {
        BigDecimal negativeAmount = BigDecimal.valueOf(-100.00);


        assertThrows(InvalidAmountException.class, () -> {
            accountService.withdraw(TEST_ACCOUNT_ID, negativeAmount);
        }, "Should throw exception for negative withdrawal amount");


        verifyNoInteractions(accountRepository, transactionRepository);
    }

    @Test
    @DisplayName("Should reject withdrawal with zero amount")
    void withdrawZeroAmount_throwsException() {
        BigDecimal zeroAmount = BigDecimal.ZERO;


        assertThrows(InvalidAmountException.class, () -> {
            accountService.withdraw(TEST_ACCOUNT_ID, zeroAmount);
        }, "Should throw exception for zero withdrawal amount");
        

        verifyNoInteractions(accountRepository, transactionRepository);
    }


    // ** STATEMENT TESTS ** //

    @Test
    @DisplayName("Should return statement with all transactions")
    void getFullAccountStatementWithExistingTransactions() {
        Transaction deposit = new Transaction(testAccount, null, TransactionType.DEPOSIT, 
            BigDecimal.valueOf(200.00), BigDecimal.valueOf(700.00));
        Transaction withdrawal = new Transaction(testAccount, null, TransactionType.WITHDRAWAL, 
            BigDecimal.valueOf(100.00), BigDecimal.valueOf(600.00));
        
        List<Transaction> transactions = Arrays.asList(deposit, withdrawal);
        
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.findAllTransactionsForAccount(TEST_ACCOUNT_ID)).thenReturn(transactions);


        StatementResponse response = accountService.getFullAccountStatement(TEST_ACCOUNT_ID);
        assertEquals(2, response.transactions().size(), "Should return all transactions");

        TransactionResponse firstTx = response.transactions().get(0);
        assertEquals(TransactionType.DEPOSIT, firstTx.type(), "First transaction should be deposit");
        assertEquals(BigDecimal.valueOf(200.00), firstTx.amount(), "Deposit amount should match");
        assertEquals(BigDecimal.valueOf(700.00), firstTx.balanceAfter(), "Balance after deposit should match");
        
        TransactionResponse secondTx = response.transactions().get(1);
        assertEquals(TransactionType.WITHDRAWAL, secondTx.type(), "Second transaction should be withdrawal");
        assertEquals(BigDecimal.valueOf(100.00), secondTx.amount(), "Withdrawal amount should match");
        assertEquals(BigDecimal.valueOf(600.00), secondTx.balanceAfter(), "Balance after withdrawal should match");


        verify(transactionRepository).findAllTransactionsForAccount(TEST_ACCOUNT_ID);
    }

    @Test
    @DisplayName("Should return empty (not null) when no transactions exist")
    void getFullAccountStatementWithNoTransactions() {
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.findAllTransactionsForAccount(TEST_ACCOUNT_ID)).thenReturn(Collections.emptyList());


        StatementResponse response = accountService.getFullAccountStatement(TEST_ACCOUNT_ID);
        assertNotNull(response, "Response should not be null");
        assertEquals(INITIAL_BALANCE, response.currentBalance(), "Balance should match account balance");
        assertNotNull(response.transactions(), "Transactions list should not be null");
        assertTrue(response.transactions().isEmpty(), "Transactions list should be empty");
        

        verify(transactionRepository).findAllTransactionsForAccount(TEST_ACCOUNT_ID);
    }
}