package com.aryan.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aryan.wallet.entity.WalletTransaction;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(Long fromId, Long toId);
}