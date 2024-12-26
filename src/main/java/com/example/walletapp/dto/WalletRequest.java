package com.example.walletapp.dto;

//import com.example.walletapp.OperationTypeDeserializer;


import static com.example.walletapp.constants.ErrorMessages.AMOUNT_CANNOT_BE_NULL;
import static com.example.walletapp.constants.ErrorMessages.AMOUNT_MUST_BE_POSITIVE;
import static com.example.walletapp.constants.ErrorMessages.OPERATION_TYPE_MUST_BE_PROVIDED;
import static com.example.walletapp.constants.ErrorMessages.WALLET_ID_MUST_NOT_BE_NULL;

import com.example.walletapp.OperationTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public class WalletRequest {

    @NotNull(message = WALLET_ID_MUST_NOT_BE_NULL)
    private UUID walletId;

    @NotNull(message = OPERATION_TYPE_MUST_BE_PROVIDED)
    @JsonDeserialize(using = OperationTypeDeserializer.class)
    private OperationType operationType;

    @NotNull(message = AMOUNT_CANNOT_BE_NULL)
    @Positive(message = AMOUNT_MUST_BE_POSITIVE)
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
