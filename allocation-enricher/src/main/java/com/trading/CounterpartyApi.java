package com.trading;

interface CounterpartyApi {
    Exchange getExchange(String micCode);
    String getPartyName(String id);
}
