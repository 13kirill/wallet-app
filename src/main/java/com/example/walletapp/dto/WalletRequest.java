package com.example.walletapp.dto;

import com.example.walletapp.OperationTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public class WalletRequest {

    @NotNull(message = "walletId cannot be null")
    private UUID walletId;

    @NotNull(message = "operationType cannot be null")
    @JsonDeserialize(using = OperationTypeDeserializer.class)
    private OperationType operationType;

    @NotNull(message = "amount cannot be null")
    @Positive(message = "amount must be positive")
    private BigDecimal amount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
