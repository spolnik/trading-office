package com.trading;

import java.io.Serializable;

public class Instrument implements Serializable {

    private String symbol;
    private String name;
    private String currency;

    public static Instrument empty() {
        return new Instrument();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
