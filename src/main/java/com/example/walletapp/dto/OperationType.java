package com.example.walletapp.dto;

import static com.example.walletapp.constants.ErrorMessages.DEPOSIT_SUCCESSFUL;
import static com.example.walletapp.constants.ErrorMessages.WITHDRAWAL_SUCCESSFUL;

public enum OperationType {
    DEPOSIT(DEPOSIT_SUCCESSFUL),
    WITHDRAW(WITHDRAWAL_SUCCESSFUL);

    private final String message;

    OperationType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

