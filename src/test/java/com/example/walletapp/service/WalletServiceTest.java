package com.example.walletapp.service;

import com.example.walletapp.dto.OperationType;
import com.example.walletapp.dto.WalletRequest;
import com.example.walletapp.entity.Wallet;
import com.example.walletapp.exception.InsufficientFundsException;
import com.example.walletapp.exception.UnsupportedOperationTypeException;
import com.example.walletapp.exception.WalletNotFoundException;
import com.example.walletapp.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private Wallet wallet;
    private UUID walletId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(100.0);
        walletId = wallet.getId();
    }

    @Test
    void testProcessWalletOperationDepositSuccess() {
        // Arrange
        WalletRequest request = new WalletRequest();
        request.setWalletId(walletId);
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(50.0);

        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);

        // Act
        Wallet updatedWallet = walletService.processWalletOperation(request);

        // Assert
        assertEquals(150.0, updatedWallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testProcessWalletOperationWithdrawSuccess() {
        // Arrange
        WalletRequest request = new WalletRequest();
        request.setWalletId(walletId);
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(50.0);

        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);

        // Act
        Wallet updatedWallet = walletService.processWalletOperation(request);

        // Assert
        assertEquals(50.0, updatedWallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testProcessWalletOperationWithdrawInsufficientFunds() {
        // Arrange
        WalletRequest request = new WalletRequest();
        request.setWalletId(walletId);
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(200.0); // Insufficient funds

        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        // Act & Assert
        Exception exception = assertThrows(InsufficientFundsException.class,
            () -> walletService.processWalletOperation(request));
        assertEquals("Insufficient funds in wallet: " + walletId, exception.getMessage());
        verify(walletRepository, never()).save(wallet);
    }

    @Test
    void testGetWalletBalanceSuccess() {
        // Arrange
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        // Act
        double balance = walletService.getWalletBalance(walletId);

        // Assert
        assertEquals(100.0, balance);
    }

    @Test
    void testGetWalletBalanceWalletNotFound() {
        // Arrange
        UUID nonExistentWalletId = UUID.randomUUID();
        when(walletRepository.findById(nonExistentWalletId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(WalletNotFoundException.class,
            () -> walletService.getWalletBalance(nonExistentWalletId));
        assertEquals("Wallet not found: " + nonExistentWalletId, exception.getMessage());
    }

    @Test
    void testCreateNewWalletSuccess() {
        // Arrange
        double initialBalance = 200.0;
        Wallet newWallet = new Wallet();
        newWallet.setId(UUID.randomUUID());
        newWallet.setBalance(initialBalance);

        when(walletRepository.save(any(Wallet.class))).thenReturn(newWallet);

        // Act
        Wallet createdWallet = walletService.createNewWallet(initialBalance);

        // Assert
        assertNotNull(createdWallet);
        assertEquals(initialBalance, createdWallet.getBalance());
        verify(walletRepository).save(any(Wallet.class));
    }
}
