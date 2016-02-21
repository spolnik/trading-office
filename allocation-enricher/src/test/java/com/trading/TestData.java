package com.trading;

import java.math.BigDecimal;

class TestData {
    static EnrichedAllocation allocationReport() {
        EnrichedAllocation allocationReport = new EnrichedAllocation();

        allocationReport.setAllocationId("1234567");
        allocationReport.setSecurityId("2000019");
        allocationReport.setCounterpartyId("CUSTUS");
        allocationReport.setExecutingPartyId("TROF");
        allocationReport.setMicCode("XNAS");

        return allocationReport;
    }

    static Instrument instrument() {
        Instrument instrument = new Instrument();

        instrument.setName("Amazon.com, Inc. Stocks");
        instrument.setCurrency("USD");
        instrument.setExchange("NMS");
        instrument.setSymbol("AMZN");
        instrument.setPrice(BigDecimal.valueOf(2.12));

        return instrument;
    }

    static Exchange exchange() {
        Exchange exchange = new Exchange();

        exchange.setAcronym("NASDAQ");
        exchange.setCity("NEW YORK");
        exchange.setCountry("UNITED STATES OF AMERICA");
        exchange.setCountryCode("US");
        exchange.setName("NASDAQ - ALL MARKETS");

        return exchange;
    }
}
