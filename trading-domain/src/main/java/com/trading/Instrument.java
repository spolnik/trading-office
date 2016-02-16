package com.trading;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Instrument implements Serializable {

    private String symbol;
    private String name;
    private String currency;
    private String exchange;
    private BigDecimal price;

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

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Instrument that = (Instrument) o;

        return Objects.equals(symbol, that.symbol)
                && Objects.equals(currency, that.currency)
                && Objects.equals(exchange, that.exchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, currency, exchange);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("symbol", symbol)
                .add("name", name)
                .add("currency", currency)
                .add("exchange", exchange)
                .add("price", price)
                .toString();
    }
}
