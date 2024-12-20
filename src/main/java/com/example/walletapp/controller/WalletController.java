package com.example.walletapp.controller;

import com.example.walletapp.dto.OperationType;
import com.example.walletapp.dto.WalletRequest;
import com.example.walletapp.entity.Wallet;
import com.example.walletapp.exception.InsufficientFundsException;
import com.example.walletapp.exception.UnsupportedOperationTypeException;
import com.example.walletapp.exception.WalletNotFoundException;
import com.example.walletapp.service.WalletService;
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
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<String> updateWallet(@RequestBody WalletRequest request) {
        try {
            if (request.getOperationType() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Operation type must be provided");
            }

            walletService.processWalletOperation(request);

            String operationMessage = request.getOperationType() == OperationType.DEPOSIT
                ? "Deposit successful"
                : "Withdrawal successful";
            return ResponseEntity.ok(operationMessage);

        } catch (WalletNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (InsufficientFundsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (UnsupportedOperationTypeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Unsupported operation type: " + request.getOperationType());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Wallet> createWallet(@RequestBody WalletRequest request) {
        Wallet newWallet = walletService.createNewWallet(request.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(newWallet);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<?> getWalletBalance(@PathVariable UUID walletId) {
        try {
            double balance = walletService.getWalletBalance(walletId);
            return ResponseEntity.ok(balance);
        } catch (WalletNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
