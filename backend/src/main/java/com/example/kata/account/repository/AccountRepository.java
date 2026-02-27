package com.example.kata.account.repository;

import com.example.kata.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // No specific implementation. JpaRepository gives basic fucntionalities.
}