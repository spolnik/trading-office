package com.trading;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestData {

    static AllocationReport allocationReport(String allocationId) {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId(allocationId);
        allocationReport.setTransactionType(TransactionType.NEW);
        allocationReport.setSecurityId("2000019");
        allocationReport.setSecurityIdSource(SecurityIDSource.SEDOL);

        allocationReport.setPrice(BigDecimal.valueOf(45.124));
        allocationReport.setQuantity(1234);
        allocationReport.setStatus(AllocationStatus.ACCEPTED);
        allocationReport.setTradeDate(ZonedDateTime.of(2016, 6, 3, 0, 0, 0, 0, ZoneId.of("GMT")));
        allocationReport.setTradeSide(TradeSide.BUY);

        allocationReport.setInstrument(instrument());

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
