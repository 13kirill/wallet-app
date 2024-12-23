package com.example.walletapp.dto;

public enum OperationType {
    DEPOSIT,
    WITHDRAW;

    public String getOperation() {
        return name();
    }
}
