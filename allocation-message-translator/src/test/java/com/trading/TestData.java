package com.trading;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

class TestData {
    static final String FIXML_ALLOCATION_REPORT_MESSAGE = "<FIXML><AllocRpt TransTyp=\"0\" RptID=\"%s\" GrpID=\"1234567\" AvgPxGrpID=\"AP101\" Stat=\"3\" BizDt=\"2016-06-03\" RptTyp=\"2\" Qty=\"200\" AvgPxInd=\"2\" Side=\"1\" TrdTyp=\"0\" TrdSubTyp=\"5\" AvgPx=\"57.5054673\" TrdDt=\"2016-06-03\" RndPx=\"57.51\" GrpQty=\"350\" RemQty=\"150\" InptDev=\"API\"><Hdr SID=\"ICE\" TID=\"GUF\"/><Instrmt ID=\"2000019\" Src=\"2\"/><Pty R=\"1\" Src=\"D\" ID=\"TROF\" /><Pty R=\"22\" Src=\"G\" ID=\"XNAS\" /><Pty R=\"3\" Src=\"D\" ID=\"CUSTUS\" /><Amt Typ=\"CRES\"  Amt=\"10.93\" Ccy=\"EUR\"/><Alloc IndAllocID2=\"2827379\" Qty=\"200\"></Alloc> </AllocRpt></FIXML>";

    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("1234567");
        allocationReport.setTransactionType(TransactionType.NEW);
        allocationReport.setSecurityId("2000019");
        allocationReport.setInstrumentType(InstrumentType.SEDOL);
        allocationReport.setTradeSide(TradeSide.BUY);
        allocationReport.setQuantity(200);
        allocationReport.setStatus(AllocationStatus.RECEIVED);
        allocationReport.setPrice(BigDecimal.valueOf(57.5054673));
        allocationReport.setTradeDate(ZonedDateTime.of(2016, 6, 3, 0, 0, 0, 0, ZoneId.of("GMT")));
        allocationReport.setExchange(exchange());
        allocationReport.setCounterparty(counterparty());
        allocationReport.setExecutingParty(executingParty());

        return allocationReport;
    }

    private static Exchange exchange() {
        Exchange exchange = new Exchange();
        exchange.setMic("XNAS");

        return exchange;
    }

    private static Party counterparty() {
        Party counterparty = new Party();
        counterparty.setId("CUSTUS");

        return counterparty;
    }

    private static Party executingParty() {
        Party executingParty = new Party();
        executingParty.setId("TROF");

        return executingParty;
    }
}
