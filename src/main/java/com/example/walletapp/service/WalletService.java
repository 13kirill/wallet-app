package com.example.walletapp.service;

import static com.example.walletapp.constants.ErrorMessages.INSUFFICIENT_FUNDS_IN_WALLET;
import static com.example.walletapp.constants.ErrorMessages.UNSUPPORTED_OPERATION_TYPE;
import static com.example.walletapp.constants.ErrorMessages.WALLET_NOT_FOUND;

import com.example.walletapp.dto.WalletRequest;
import com.example.walletapp.entity.Wallet;
import com.example.walletapp.exception.InsufficientFundsException;
import com.example.walletapp.exception.UnsupportedOperationTypeException;
import com.example.walletapp.exception.WalletNotFoundException;
import com.example.walletapp.repository.WalletRepository;
import java.math.BigDecimal;
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
            .orElseThrow(() -> new WalletNotFoundException(WALLET_NOT_FOUND + request.getWalletId()));

        switch (request.getOperationType()) {
            case DEPOSIT:
                wallet.setBalance(wallet.getBalance().add(request.getAmount()));
                break;
            case WITHDRAW:
                if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                    throw new InsufficientFundsException(INSUFFICIENT_FUNDS_IN_WALLET + request.getWalletId());
                }
                wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
                break;
            default:
                throw new UnsupportedOperationTypeException(UNSUPPORTED_OPERATION_TYPE + request.getOperationType());
        }

        return walletRepository.save(wallet);
    }


    public BigDecimal getWalletBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
            .orElseThrow(() -> new WalletNotFoundException(WALLET_NOT_FOUND + walletId));
        return wallet.getBalance();
    }

    @Transactional
    public Wallet getWallet(UUID walletId) {
        return walletRepository.findByIdWithLock(walletId)
            .orElseThrow(() -> new WalletNotFoundException(WALLET_NOT_FOUND + walletId));
    }

    @Transactional
    public Wallet createNewWallet(BigDecimal initialBalance) {
        Wallet newWallet = new Wallet();
        newWallet.setBalance(initialBalance);
        return walletRepository.save(newWallet);
    }
}
