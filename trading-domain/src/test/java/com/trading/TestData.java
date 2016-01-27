package com.trading;

class TestData {

    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("12345");
        allocationReport.setTransactionType(TransactionType.NEW);
        allocationReport.setSecurityId("54321");
        allocationReport.setSecurityIdSource(SecurityIDSource.SEDOL);

        return allocationReport;
    }
}
