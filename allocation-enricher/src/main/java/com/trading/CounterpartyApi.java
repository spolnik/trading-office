package com.trading;

public interface CounterpartyApi {
    Exchange getExchange(String micCode);
    Party getParty(String id);
}
