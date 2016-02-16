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
    private Party counterparty;
    private Party executingParty;
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

    public Party getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(Party counterparty) {
        this.counterparty = counterparty;
    }

    public Party getExecutingParty() {
        return executingParty;
    }

    public void setExecutingParty(Party executingParty) {
        this.executingParty = executingParty;
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
                .add("counterparty", counterparty)
                .add("executingParty", executingParty)
                .toString();
    }
}
