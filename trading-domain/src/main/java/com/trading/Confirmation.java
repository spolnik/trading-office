package com.trading;

import java.io.Serializable;

public class Confirmation implements Serializable {

    private AllocationReport allocationReport;
    private byte[] content;

    public static final Confirmation EMPTY_CONFIRMATION = new Confirmation();

    static {
        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setAllocationId("#empty");
        allocationReport.setTransactionType(TransactionType.UNSUPPORTED);
        allocationReport.setMessageStatus(MessageStatus.CONFIRMED);

        EMPTY_CONFIRMATION.setAllocationReport(allocationReport);
    }

    public AllocationReport getAllocationReport() {
        return allocationReport;
    }

    public void setAllocationReport(AllocationReport allocationReport) {
        this.allocationReport = allocationReport;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Confirmation that = (Confirmation) o;

        return allocationReport != null
                ? allocationReport.equals(that.allocationReport)
                : that.allocationReport == null;

    }

    @Override
    public int hashCode() {
        return allocationReport != null
                ? allocationReport.hashCode()
                : 0;
    }

    @Override
    public String toString() {
        return "Confirmation{" +
                "allocationReport=" + allocationReport +
                '}';
    }

    public String id() {
        return allocationReport.getAllocationId();
    }
}
