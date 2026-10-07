package com.aryan.wallet.service;

import com.aryan.wallet.entity.Account;
import com.aryan.wallet.entity.WalletTransaction;
import com.aryan.wallet.exception.InsufficientBalanceException;
import com.aryan.wallet.repository.AccountRepository;
import com.aryan.wallet.repository.WalletTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    private AccountRepository accountRepository;
    private WalletTransactionRepository transactionRepository;
    private WalletService service;
    private Account from;
    private Account to;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        transactionRepository = Mockito.mock(WalletTransactionRepository.class);
        service = new WalletService(accountRepository, transactionRepository);

        from = new Account("A", "a@example.com");
        from.setBalance(new BigDecimal("500.00"));
        to = new Account("B", "b@example.com");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(to));
        when(transactionRepository.save(any(WalletTransaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void transferMovesMoney() {
        service.transfer(1L, 2L, new BigDecimal("200.00"));
        assertEquals(new BigDecimal("300.00"), from.getBalance());
        assertEquals(new BigDecimal("200.00"), to.getBalance());
    }

    @Test
    void transferFailsWhenBalanceTooLow() {
        assertThrows(InsufficientBalanceException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("900.00")));
        assertEquals(new BigDecimal("500.00"), from.getBalance());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferToSameAccountIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 1L, new BigDecimal("10.00")));
    }
}