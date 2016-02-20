package com.trading;

@FunctionalInterface
public interface ExchangeRepository {
    CsvExchange getByMicCode(String micCode);
}
