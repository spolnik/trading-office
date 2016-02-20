package com.trading;

import java.math.BigDecimal;

public class TestData {

    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("12345");
        allocationReport.setSecurityId("54321");
        allocationReport.setInstrumentCurrency("USD");
        allocationReport.setInstrumentExchange("NASDAQ");
        allocationReport.setInstrumentName("AMAZON STOCKS");
        allocationReport.setInstrumentSymbol("AMZN");
        allocationReport.setTradeSide("BUY");
        allocationReport.setTradeDate("2016-06-03");

        allocationReport.setQuantity(10);
        allocationReport.setPrice(BigDecimal.valueOf(7.89));

        allocationReport.setExchangeAcronym("NASDAQ");
        allocationReport.setExchangeCity("NEW YORK");
        allocationReport.setCountry("UNITED STATES OF AMERICA");
        allocationReport.setCountryCode("US");
        allocationReport.setExchangeName("NASDAQ - ALL MARKETS");
        allocationReport.setMicCode("XNAS");
        allocationReport.setCounterpartyId("CUSTUK");
        allocationReport.setExecutingPartyId("TROF");

        return allocationReport;
    }
}
