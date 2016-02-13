package com.trading;

@FunctionalInterface
public interface ConfirmationApi {
    ConfirmationType confirmationTypeFor(String micCode);
}
