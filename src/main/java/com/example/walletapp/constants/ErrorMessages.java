package com.example.walletapp.constants;

public final class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String DEPOSIT_SUCCESSFUL = "Deposit successful";
    public static final String WITHDRAWAL_SUCCESSFUL = "Withdrawal successful";

    public static final String AMOUNT_CANNOT_BE_NULL = "amount cannot be null";
    public static final String AMOUNT_MUST_BE_POSITIVE = "amount must be positive";
    public static final String WALLET_ID_MUST_NOT_BE_NULL = "walletId must not be null";
    public static final String OPERATION_TYPE_MUST_BE_PROVIDED = "operationType must be provided";
    public static final String UNKNOWN_TYPE = "unknown type";
    public static final String PARAMETER_MUST_BE_OF_TYPE_INVALID_VALUE = "Parameter '%s' must be of type %s. Invalid value: %s";
    public static final String INVALID_UUID_FORMAT_FOR_WALLET_ID = "Invalid UUID format for walletId";
    public static final String INVALID_DATA_FORMAT = "Invalid data format";
    public static final String WALLET_NOT_FOUND = "Wallet not found: ";
    public static final String INSUFFICIENT_FUNDS_IN_WALLET = "Insufficient funds in wallet: ";
    public static final String UNSUPPORTED_OPERATION_TYPE = "Unsupported operation type: ";
    public static final String OPERATION_TYPE_CANNOT_BE_EMPTY = "Operation type cannot be empty";
}
