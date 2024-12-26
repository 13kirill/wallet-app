package com.example.walletapp;

import static com.example.walletapp.constants.ErrorMessages.OPERATION_TYPE_CANNOT_BE_EMPTY;
import static com.example.walletapp.constants.ErrorMessages.UNSUPPORTED_OPERATION_TYPE;

import com.example.walletapp.dto.OperationType;
import com.example.walletapp.exception.UnsupportedOperationTypeException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class OperationTypeDeserializer extends JsonDeserializer<OperationType> {

    @Override
    public OperationType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().trim();

        if (value.isEmpty()) {
            throw new UnsupportedOperationTypeException(OPERATION_TYPE_CANNOT_BE_EMPTY);
        }

        try {
            return OperationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationTypeException(UNSUPPORTED_OPERATION_TYPE + value);
        }
    }
}
