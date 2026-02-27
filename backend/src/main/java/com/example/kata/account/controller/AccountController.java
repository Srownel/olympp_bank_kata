package com.example.kata.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.kata.account.DTO.AccountResponse;
import com.example.kata.account.DTO.StatementResponse;
import com.example.kata.account.DTO.TransactionRequest;
import com.example.kata.account.DTO.TransactionResponse;
import com.example.kata.account.service.AccountService;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")  // Quick and dirty for local instance. Not good practice for actual project.
public class AccountController {
    private AccountService accountService;

    // Single constructor, auto injection. No need for autowired.
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable long accountId) {
        AccountResponse accountResponse = accountService.getAccountInfo(accountId);

        return ResponseEntity.ok(accountResponse);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable long accountId, @RequestBody TransactionRequest request) {
        TransactionResponse transationResponse = accountService.deposit(accountId, request.getAmount());

        return ResponseEntity.ok(transationResponse);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable long accountId, @RequestBody TransactionRequest request) {
        // TODO proper error handling
        try {
            TransactionResponse transationResponse = accountService.withdraw(accountId, request.getAmount());
            
            return ResponseEntity.ok(transationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // terribly temporary
        }
    }

    @GetMapping("/{accountId}/statement")
    public ResponseEntity<StatementResponse> getStatement(@PathVariable long accountId) {
        StatementResponse statementresponse = accountService.getFullAccountStatement(accountId);

        return ResponseEntity.ok(statementresponse);
    }
}
