package com.example.walletapp.exception;

import static com.example.walletapp.constants.ErrorMessages.INVALID_DATA_FORMAT;
import static com.example.walletapp.constants.ErrorMessages.INVALID_UUID_FORMAT_FOR_WALLET_ID;
import static com.example.walletapp.constants.ErrorMessages.PARAMETER_MUST_BE_OF_TYPE_INVALID_VALUE;
import static com.example.walletapp.constants.ErrorMessages.UNKNOWN_TYPE;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnsupportedOperationTypeException.class)
    public ResponseEntity<String> handleUnsupportedOperationType(UnsupportedOperationTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed");
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<String> handleWalletNotFoundException(WalletNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : UNKNOWN_TYPE;
        String errorMessage = String.format(PARAMETER_MUST_BE_OF_TYPE_INVALID_VALUE,
            parameterName, expectedType, ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException ex) {
        if (ex.getTargetType() == UUID.class) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(INVALID_UUID_FORMAT_FOR_WALLET_ID);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(INVALID_DATA_FORMAT);
    }
}
