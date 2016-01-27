package com.trading;

import java.io.Serializable;

public class AllocationReport implements Serializable {

    private String allocationId;
    private TransactionType transactionType;
    private String securityId;
    private SecurityIDSource securityIdSource;
    private Instrument instrument;

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

    @Override
    public String toString() {
        return String.format(
                "AllocationReport{allocationId='%s', transactionType=%s, securityId='%s', securityIdSource=%s, instrument=%s}",
                allocationId, transactionType, securityId, securityIdSource, instrument
        );
    }
}
