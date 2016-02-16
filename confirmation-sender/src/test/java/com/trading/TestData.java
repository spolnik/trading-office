package com.trading;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

class TestData {

    static AllocationReport allocationReport(String allocationId) {
        TradeSide tradeSide = TradeSide.BUY;
        return allocationReport(allocationId, tradeSide);
    }

    static AllocationReport allocationReport(String allocationId, TradeSide tradeSide) {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId(allocationId);
        allocationReport.setSecurityId("2000019");

        allocationReport.setPrice(BigDecimal.valueOf(45.124));
        allocationReport.setQuantity(1234);
        allocationReport.setTradeDate(ZonedDateTime.of(2016, 6, 3, 0, 0, 0, 0, ZoneId.of("GMT")));
        allocationReport.setTradeSide(tradeSide);

        allocationReport.setInstrument(instrument());

        Exchange exchange = new Exchange();
        exchange.setMic("XNAS");
        allocationReport.setExchange(exchange);

        return allocationReport;
    }

    private static Instrument instrument() {
        Instrument instrument = new Instrument();

        instrument.setCurrency("USD");
        instrument.setExchange("XNAS");
        instrument.setName("AMAZON STOCKS");
        instrument.setSymbol("AMZN");

        return instrument;
    }
}
