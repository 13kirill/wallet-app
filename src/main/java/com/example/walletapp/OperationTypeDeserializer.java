package com.example.walletapp;

import com.example.walletapp.dto.OperationType;
import com.example.walletapp.exception.UnsupportedOperationTypeException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class OperationTypeDeserializer extends JsonDeserializer<OperationType> {

    @Override
    public OperationType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase();
        try {
            return OperationType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationTypeException("Unsupported operation type: " + value);
        }
    }
}
