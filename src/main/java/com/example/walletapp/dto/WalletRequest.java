package com.example.walletapp.dto;

import com.example.walletapp.OperationTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;

public class WalletRequest {

    private UUID walletId;
    @JsonDeserialize(using = OperationTypeDeserializer.class)
    private OperationType operationType;
    private double amount;

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
