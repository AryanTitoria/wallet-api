package com.aryan.wallet.service;

import com.aryan.wallet.dto.CreateAccountRequest;
import com.aryan.wallet.entity.Account;
import com.aryan.wallet.exception.AccountNotFoundException;
import com.aryan.wallet.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(CreateAccountRequest request) {
        if (accountRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        return accountRepository.save(new Account(request.name(), request.email()));
    }

    public Account get(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }
}