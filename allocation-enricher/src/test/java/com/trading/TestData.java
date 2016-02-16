package com.trading;

class TestData {
    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("1234567");
        allocationReport.setTransactionType(TransactionType.NEW);
        allocationReport.setSecurityId("2000019");

        Party counterparty = new Party();
        counterparty.setId("CUSTUS");
        allocationReport.setCounterparty(counterparty);

        Party executingParty = new Party();
        executingParty.setId("TROF");
        allocationReport.setExecutingParty(executingParty);

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

    static Party executingParty() {
        Party party = new Party();
        party.setId("TROF");
        party.setName("Trading Office Ltd.");
        return party;
    }
}
