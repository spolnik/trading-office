package com.trading;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class AllocationReport implements Serializable {

    private String allocationId;
    private TransactionType transactionType;
    private String securityId;
    private InstrumentType instrumentType;
    private Instrument instrument;
    private TradeSide tradeSide;
    private ZonedDateTime tradeDate;
    private int quantity;
    private AllocationStatus status;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
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

    public ZonedDateTime getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(ZonedDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AllocationStatus getStatus() {
        return status;
    }

    public void setStatus(AllocationStatus status) {
        this.status = status;
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
                .add("transactionType", transactionType)
                .add("securityId", securityId)
                .add("instrumentType", instrumentType)
                .add("instrument", instrument)
                .add("tradeSide", tradeSide)
                .add("tradeDate", tradeDate)
                .add("quantity", quantity)
                .add("status", status)
                .add("price", price)
                .add("exchange", exchange)
                .add("counterparty", counterparty)
                .add("executingParty", executingParty)
                .toString();
    }
}
