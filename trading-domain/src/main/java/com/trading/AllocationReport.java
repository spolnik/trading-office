package com.trading;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.math.BigDecimal;

public class AllocationReport implements Serializable {

    private String allocationId;
    private String securityId;
    private Instrument instrument;
    private TradeSide tradeSide;
    private String tradeDate;
    private int quantity;
    private BigDecimal price;
    private String counterpartyId;
    private String counterpartyName;
    private String executingPartyId;
    private String executingPartyName;
    private Exchange exchange;

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

    public TradeSide getTradeSide() {
        return tradeSide;
    }

    public void setTradeSide(TradeSide tradeSide) {
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

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("allocationId", allocationId)
                .add("securityId", securityId)
                .add("instrument", instrument)
                .add("tradeSide", tradeSide)
                .add("tradeDate", tradeDate)
                .add("quantity", quantity)
                .add("price", price)
                .add("exchange", exchange)
                .add("counterpartyId", counterpartyId)
                .add("counterpartyName", counterpartyName)
                .add("executingPartyId", executingPartyId)
                .add("executingPartyName", executingPartyName)
                .toString();
    }
}
