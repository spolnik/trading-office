package com.trading;

import java.io.Serializable;

public class AllocationReport implements Serializable {

    private String allocationId;
    private TransactionType transactionType;
    private MessageStatus messageStatus;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AllocationReport that = (AllocationReport) o;

        return allocationId != null
                ? allocationId.equals(that.allocationId)
                : that.allocationId == null;
    }

    @Override
    public int hashCode() {
        return allocationId != null
                ? allocationId.hashCode()
                : 0;
    }

    @Override
    public String toString() {
        return String.format(
                "AllocationReport{allocationId='%s', transactionType='%s', messageStatus=%s}",
                allocationId,
                transactionType,
                messageStatus
        );
    }
}
