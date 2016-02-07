package com.trading;

interface CounterpartyApi {
    Exchange getExchange(String micCode);
    Party getParty(String id);
}
