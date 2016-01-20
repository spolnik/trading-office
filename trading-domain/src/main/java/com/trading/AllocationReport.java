package com.trading;

import java.io.Serializable;

public class AllocationReport implements Serializable {

    private String allocationId;
    private TransactionType transactionType;
    private MessageStatus messageStatus;
    private String securityId;
    private SecurityIDSource securityIdSource;

    public AllocationReport() {
        messageStatus = MessageStatus.NEW;
    }

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

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
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

    @Override
    public String toString() {
        return String.format(
                "AllocationReport{allocationId='%s', transactionType=%s, messageStatus=%s, securityId='%s', securityIdSource=%s}",
                allocationId, transactionType, messageStatus, securityId, securityIdSource
        );
    }
}
