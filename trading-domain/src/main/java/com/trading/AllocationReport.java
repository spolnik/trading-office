package com.trading;

import java.io.Serializable;
import java.math.BigDecimal;

public class AllocationReport implements Serializable {

    private String allocationId;
    private String securityId;
    private Instrument instrument;
    private String tradeSide;
    private String tradeDate;
    private int quantity;
    private BigDecimal price;
    private String counterpartyId;
    private String counterpartyName;
    private String executingPartyId;
    private String executingPartyName;
    private String micCode;
    private String country;
    private String countryCode;

    private String exchangeName;
    private String exchangeAcronym;
    private String exchangeCity;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeAcronym() {
        return exchangeAcronym;
    }

    public void setExchangeAcronym(String exchangeAcronym) {
        this.exchangeAcronym = exchangeAcronym;
    }

    public String getExchangeCity() {
        return exchangeCity;
    }

    public void setExchangeCity(String exchangeCity) {
        this.exchangeCity = exchangeCity;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getTradeSide() {
        return tradeSide;
    }

    public void setTradeSide(String tradeSide) {
        this.tradeSide = tradeSide;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(String counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getExecutingPartyId() {
        return executingPartyId;
    }

    public void setExecutingPartyId(String executingPartyId) {
        this.executingPartyId = executingPartyId;
    }

    public String getExecutingPartyName() {
        return executingPartyName;
    }

    public void setExecutingPartyName(String executingPartyName) {
        this.executingPartyName = executingPartyName;
    }

    public void setMicCode(String micCode) {
        this.micCode = micCode;
    }

    public String getMicCode() {
        return micCode;
    }
}
