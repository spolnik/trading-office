package com.trading;

public class TestData {
    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("1234567");
        allocationReport.setTransactionType(TransactionType.NEW);
        allocationReport.setSecurityId("2000019");
        allocationReport.setInstrumentType(InstrumentType.SEDOL);

        return allocationReport;
    }

    static Instrument instrument() {
        Instrument instrument = new Instrument();

        instrument.setName("Amazon.com, Inc. Stocks");
        instrument.setCurrency("USD");
        instrument.setExchange("NMS");
        instrument.setSymbol("AMZN");

        return instrument;
    }
}
