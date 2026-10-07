package com.aryan.wallet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aryan.wallet.dto.DepositRequest;
import com.aryan.wallet.dto.TransferRequest;
import com.aryan.wallet.entity.WalletTransaction;
import com.aryan.wallet.service.WalletService;

import jakarta.validation.Valid;

@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<WalletTransaction> deposit(@PathVariable Long id,
                                                     @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.deposit(id, request.amount()));
    }

    @PostMapping("/transfers")
    public ResponseEntity<WalletTransaction> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                walletService.transfer(request.fromAccountId(), request.toAccountId(), request.amount()));
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<WalletTransaction> history(@PathVariable Long id) {
        return walletService.history(id);
    }
}