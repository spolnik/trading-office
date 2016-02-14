package com.trading;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

class TestData {

    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("12345");
        allocationReport.setTransactionType(TransactionType.NEW);
        allocationReport.setSecurityId("54321");
        allocationReport.setInstrumentType(InstrumentType.SEDOL);
        allocationReport.setInstrument(instrument());
        allocationReport.setTradeSide(TradeSide.BUY);

        ZonedDateTime tradeDate = ZonedDateTime.of(2016, 1, 10, 10, 10, 10, 0, ZoneId.of("GMT"));
        allocationReport.setTradeDate(tradeDate);

        allocationReport.setQuantity(10);
        allocationReport.setStatus(AllocationStatus.RECEIVED);
        allocationReport.setPrice(BigDecimal.valueOf(7.89));

        allocationReport.setExchange(exchange());
        allocationReport.setCounterparty(counterparty());
        allocationReport.setExecutingParty(executingParty());

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

    private static Exchange exchange() {
        Exchange exchange = new Exchange();

        exchange.setAcronym("NASDAQ");
        exchange.setCity("NEW YORK");
        exchange.setCountry("UNITED STATES OF AMERICA");
        exchange.setComments("Comment");
        exchange.setCountryCode("US");
        exchange.setName("NASDAQ - ALL MARKETS");
        exchange.setMic("XNAS");
        exchange.setWebsite("WWW.NASDAQ.COM");

        return exchange;
    }

    private static Party counterparty() {
        Party party = new Party();
        party.setId("CUSTAU");
        party.setName("Customer Australia Pty Ltd.");
        return party;
    }

    private static Party executingParty() {
        Party party = new Party();
        party.setId("TROF");
        party.setName("Trading Office Ltd.");
        return party;
    }
}
