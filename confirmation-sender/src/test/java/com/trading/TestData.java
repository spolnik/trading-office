package com.trading;

import java.math.BigDecimal;

class TestData {

    static Confirmation allocationReport(String allocationId) {
        return allocationReport(allocationId, "BUY");
    }

    static Confirmation allocationReport(String allocationId, String tradeSide) {
        Confirmation allocationReport = new Confirmation();

        allocationReport.setAllocationId(allocationId);
        allocationReport.setSecurityId("2000019");

        allocationReport.setPrice(BigDecimal.valueOf(45.124));
        allocationReport.setQuantity(1234);
        allocationReport.setTradeDate("2016-06-03");
        allocationReport.setTradeSide(tradeSide);

        allocationReport.setInstrumentCurrency("USD");
        allocationReport.setInstrumentExchange("XNAS");
        allocationReport.setInstrumentName("AMAZON STOCKS");
        allocationReport.setInstrumentSymbol("AMZN");
        allocationReport.setMicCode("XNAS");

        return allocationReport;
    }
}
