package com.trading;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.math.BigDecimal;

public class Allocation implements Serializable {

    public static final String BUY = "BUY";
    public static final String SELL = "SELL";

    private String allocationId;
    private String securityId;
    private String tradeSide;
    private String tradeDate;
    private int quantity;
    private BigDecimal price;
    private String counterpartyId;
    private String executingPartyId;
    private String micCode;

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

    public String getExecutingPartyId() {
        return executingPartyId;
    }

    public void setExecutingPartyId(String executingPartyId) {
        this.executingPartyId = executingPartyId;
    }

    public void setMicCode(String micCode) {
        this.micCode = micCode;
    }

    public String getMicCode() {
        return micCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("allocationId", allocationId)
                .add("securityId", securityId)
                .add("tradeSide", tradeSide)
                .add("tradeDate", tradeDate)
                .add("quantity", quantity)
                .add("price", price)
                .add("counterpartyId", counterpartyId)
                .add("executingPartyId", executingPartyId)
                .add("micCode", micCode)
                .toString();
    }
}
