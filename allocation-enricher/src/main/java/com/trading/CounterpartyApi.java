package com.trading;

interface CounterpartyApi {
    Exchange getExchange(String micCode);
    PartyResponse getParty(String id);
}
