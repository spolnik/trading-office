package com.trading;

import java.util.Arrays;
import java.util.Optional;

public enum TransactionType {
    NEW("0");

    private final String fixTransactionType;

    TransactionType(String fixTransactionType) {
        this.fixTransactionType = fixTransactionType;
    }

    public static TransactionType getTransactionType(String fixTransactionType) {
        Optional<TransactionType> transactionType = Arrays.asList(values())
                .stream()
                .filter(value -> value.fixTransactionType.equals(fixTransactionType))
                .findFirst();

        return transactionType.orElseThrow(
                () -> new UnsupportedOperationException("Transaction type is unsupported: " + fixTransactionType)
        );
    }
}
