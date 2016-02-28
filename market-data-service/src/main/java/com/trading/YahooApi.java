package com.trading;

@FunctionalInterface
public interface YahooApi {

    Instrument getInstrument(String symbol);
}
