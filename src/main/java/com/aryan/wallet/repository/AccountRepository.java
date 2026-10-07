package com.aryan.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aryan.wallet.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
}