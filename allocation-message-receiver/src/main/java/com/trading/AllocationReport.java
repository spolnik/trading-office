package com.trading;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.math.BigDecimal;

public class AllocationReport implements Serializable {

    public static final String BUY = "BUY";
    public static final String SELL = "SELL";

    private String allocationId;
    private String securityId;
    private String tradeSide;
    private String tradeDate;
    private int quantity;
    private BigDecimal price;
    private String counterpartyId;
    private String counterpartyName;
    private String executingPartyId;
    private String executingPartyName;
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
                .add("counterpartyName", counterpartyName)
                .add("executingPartyId", executingPartyId)
                .add("executingPartyName", executingPartyName)
                .add("micCode", micCode)
                .toString();
    }

    public String getMicCode() {
        return micCode;
    }
}
