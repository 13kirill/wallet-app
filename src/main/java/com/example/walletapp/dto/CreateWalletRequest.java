package com.example.walletapp.dto;

import static com.example.walletapp.constants.ErrorMessages.AMOUNT_CANNOT_BE_NULL;
import static com.example.walletapp.constants.ErrorMessages.AMOUNT_MUST_BE_POSITIVE;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateWalletRequest {
    @NotNull(message = AMOUNT_CANNOT_BE_NULL)
    @Positive(message = AMOUNT_MUST_BE_POSITIVE)
    private BigDecimal amount;

    // Геттеры и сеттеры
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
