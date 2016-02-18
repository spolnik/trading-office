package com.trading;

class TestData {
    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("1234567");
        allocationReport.setSecurityId("2000019");
        allocationReport.setCounterpartyId("CUSTUS");
        allocationReport.setExecutingPartyId("TROF");

        Exchange exchange = new Exchange();
        exchange.setMic("XNAS");
        allocationReport.setExchange(exchange);

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

    static PartyResponse executingParty() {
        PartyResponse party = new PartyResponse();
        party.setId("TROF");
        party.setName("Trading Office Ltd.");
        return party;
    }
}
