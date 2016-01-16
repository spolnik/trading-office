package com.trading;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

import java.io.Serializable;

@SpaceClass
public class AllocationReport implements Serializable {

    private String allocationId;
    private String tradeType;
    private MessageStatus status;

    public AllocationReport() {
        status = MessageStatus.NEW;
    }

    @SpaceId(autoGenerate = false)
    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllocationReport that = (AllocationReport) o;

        return allocationId != null ? allocationId.equals(that.allocationId) : that.allocationId == null;

    }

    @Override
    public int hashCode() {
        return allocationId != null ? allocationId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AllocationReport{" +
                "allocationId='" + allocationId + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", status=" + status +
                '}';
    }
}
