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

        return allocationReport;
    }

    private static Instrument instrument() {
        Instrument instrument = new Instrument();

        instrument.setCurrency("USD");
        instrument.setExchange("NASDAQ");
        instrument.setName("AMAZON STOCKS");
        instrument.setSymbol("AMZN");

        return instrument;
    }
}
