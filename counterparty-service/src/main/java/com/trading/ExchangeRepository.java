package com.trading;

@FunctionalInterface
public interface ExchangeRepository {
    Exchange getByMicCode(String micCode);
}
