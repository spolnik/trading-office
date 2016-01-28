package com.trading;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

public class AllocationReport implements Serializable {

    private String allocationId;
    private TransactionType transactionType;
    private String securityId;
    private SecurityIDSource securityIdSource;
    private Instrument instrument;
    private TradeSide tradeSide;

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

    public SecurityIDSource getSecurityIdSource() {
        return securityIdSource;
    }

    public void setSecurityIdSource(SecurityIDSource securityIdSource) {
        this.securityIdSource = securityIdSource;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("allocationId", allocationId)
                .add("transactionType", transactionType)
                .add("securityId", securityId)
                .add("securityIdSource", securityIdSource)
                .add("instrument", instrument)
                .add("tradeSide", tradeSide)
                .toString();
    }
}
