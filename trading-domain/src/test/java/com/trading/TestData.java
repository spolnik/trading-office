package com.trading;

import java.math.BigDecimal;

public class TestData {

    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("12345");
        allocationReport.setSecurityId("54321");
        allocationReport.setInstrument(instrument());
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

    static Instrument instrument() {
        Instrument instrument = new Instrument();

        instrument.setCurrency("USD");
        instrument.setExchange("NASDAQ");
        instrument.setName("AMAZON STOCKS");
        instrument.setSymbol("AMZN");

        return instrument;
    }
}
