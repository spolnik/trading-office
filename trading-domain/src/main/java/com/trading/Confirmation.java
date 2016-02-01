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

        EMPTY_CONFIRMATION.setAllocationReport(allocationReport);
    }

    public Confirmation() {
        // empty
    }

    public Confirmation(AllocationReport allocationReport, byte[] content) {
        this.allocationReport = allocationReport;
        this.content = content;
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
    public String toString() {
        return String.format("Confirmation{allocationReport=%s}", allocationReport);
    }

    public String id() {
        return allocationReport.getAllocationId();
    }
}
