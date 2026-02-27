package com.example.kata.account.repository;

import com.example.kata.account.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find all transactions for an account, ordered by timestamp descending
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId ORDER BY t.timestamp DESC")
    List<Transaction> findAllTransactionsForAccount(@Param("accountId") long accountId);

}