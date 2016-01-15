package com.trading;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

@SpaceClass
public class AllocationReport {

    private String allocationId;
    private String tradeType;

    @SpaceRouting
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
                '}';
    }
}
