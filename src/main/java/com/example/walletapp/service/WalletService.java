package com.example.walletapp.service;

import com.example.walletapp.dto.WalletRequest;
import com.example.walletapp.entity.Wallet;
import com.example.walletapp.exception.InsufficientFundsException;
import com.example.walletapp.exception.UnsupportedOperationTypeException;
import com.example.walletapp.exception.WalletNotFoundException;
import com.example.walletapp.repository.WalletRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public Wallet processWalletOperation(WalletRequest request) {
        Wallet wallet = walletRepository.findByIdWithLock(request.getWalletId())
            .orElseThrow(
                () -> new WalletNotFoundException("Wallet not found: " + request.getWalletId()));

        if (request.getOperationType() == null) {
            throw new IllegalArgumentException("Invalid operation type: null");
        }

        switch (request.getOperationType()) {
            case DEPOSIT:
                wallet.setBalance(wallet.getBalance() + request.getAmount());
                break;
            case WITHDRAW:
                if (wallet.getBalance() < request.getAmount()) {
                    throw new InsufficientFundsException(
                        "Insufficient funds in wallet: " + request.getWalletId());
                }
                wallet.setBalance(wallet.getBalance() - request.getAmount());
                break;
            default:
                throw new UnsupportedOperationTypeException(
                    "Unsupported operation type: " + request.getOperationType());
        }

        return walletRepository.save(wallet);
    }

    public double getWalletBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));
        return wallet.getBalance();
    }

    @Transactional
    public Wallet createNewWallet(double initialBalance) {
        Wallet newWallet = new Wallet();
        newWallet.setBalance(initialBalance);
        return walletRepository.save(newWallet);
    }
}
