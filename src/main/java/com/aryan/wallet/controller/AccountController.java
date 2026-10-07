package com.aryan.wallet.controller;

import com.aryan.wallet.dto.CreateAccountRequest;
import com.aryan.wallet.entity.Account;
import com.aryan.wallet.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(request));
    }

    @GetMapping("/{id}")
    public Account get(@PathVariable Long id) {
        return accountService.get(id);
    }
}