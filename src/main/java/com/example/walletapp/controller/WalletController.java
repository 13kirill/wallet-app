package com.example.walletapp.controller;

import com.example.walletapp.dto.CreateWalletRequest;
import com.example.walletapp.dto.OperationType;
import com.example.walletapp.dto.WalletRequest;
import com.example.walletapp.entity.Wallet;
import com.example.walletapp.exception.InsufficientFundsException;
import com.example.walletapp.exception.UnsupportedOperationTypeException;
import com.example.walletapp.exception.WalletNotFoundException;
import com.example.walletapp.service.WalletService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/operation")
    public ResponseEntity<String> processWalletOperation(@Valid @RequestBody WalletRequest request) {
        walletService.getWallet(request.getWalletId());
        walletService.processWalletOperation(request);

        String operationMessage = request.getOperationType().getMessage();
        return ResponseEntity.ok(operationMessage);
    }

    @PostMapping("/create")
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        Wallet newWallet = walletService.createNewWallet(request.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(newWallet);
    }

    @GetMapping("/balance/{walletId}")
    public ResponseEntity<BigDecimal> getWalletBalance(@PathVariable UUID walletId) {
        BigDecimal balance = walletService.getWalletBalance(walletId);
        return ResponseEntity.ok(balance);
    }
}
