package com.trading;

import java.io.Serializable;

public class Confirmation implements Serializable {

    public static final String EMAIL = "EMAIL";
    public static final String SWIFT = "SWIFT";

    private AllocationReport allocationReport;
    private byte[] content;
    private String confirmationType;

    public Confirmation() {
        // empty
    }

    public Confirmation(AllocationReport allocationReport, byte[] content, String confirmationType) {

        checkIfConfirmationTypeIsValid(confirmationType);
        this.allocationReport = allocationReport;
        this.content = content;
        this.confirmationType = confirmationType;
    }

    private void checkIfConfirmationTypeIsValid(String confirmationType) {
        if ("SWIFT".equals(confirmationType) || "EMAIL".equals(confirmationType)) {
            return;
        }

        throw new UnsupportedOperationException("Confirmation type unsupported: " + confirmationType);
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

    public String getConfirmationType() {
        return confirmationType;
    }

    public void setConfirmationType(String confirmationType) {
        this.confirmationType = confirmationType;
    }

    @Override
    public String toString() {
        return String.format("Confirmation{allocationReport=%s}", allocationReport);
    }

    public String id() {
        return allocationReport.getAllocationId();
    }
}
