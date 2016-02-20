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

        allocationReport.setExchange(exchange());
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

    static Exchange exchange() {
        Exchange exchange = new Exchange();

        exchange.setAcronym("NASDAQ");
        exchange.setCity("NEW YORK");
        exchange.setCountry("UNITED STATES OF AMERICA");
        exchange.setCountryCode("US");
        exchange.setName("NASDAQ - ALL MARKETS");
        exchange.setMic("XNAS");
        exchange.setWebsite("WWW.NASDAQ.COM");

        return exchange;
    }
}
