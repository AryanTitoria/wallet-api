package com.aryan.wallet.service;

import com.aryan.wallet.entity.*;
import com.aryan.wallet.exception.AccountNotFoundException;
import com.aryan.wallet.exception.InsufficientBalanceException;
import com.aryan.wallet.repository.AccountRepository;
import com.aryan.wallet.repository.WalletTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    private final AccountRepository accountRepository;
    private final WalletTransactionRepository transactionRepository;

    public WalletService(AccountRepository accountRepository,
                         WalletTransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public WalletTransaction deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        return transactionRepository.save(new WalletTransaction(
                TransactionType.DEPOSIT, TransactionStatus.SUCCESS, amount, null, accountId, null));
    }

    @Transactional
    public WalletTransaction transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));
        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in account " + fromId);
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accountRepository.save(from);
        accountRepository.save(to);

        return transactionRepository.save(new WalletTransaction(
                TransactionType.TRANSFER, TransactionStatus.SUCCESS, amount, fromId, toId, null));
    }

    public List<WalletTransaction> history(Long accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        return transactionRepository
                .findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(accountId, accountId);
    }
}