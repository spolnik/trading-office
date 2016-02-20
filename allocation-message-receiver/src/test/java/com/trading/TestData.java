package com.trading;

import java.math.BigDecimal;

class TestData {
    static final String FIXML_ALLOCATION_REPORT_MESSAGE = "<FIXML><AllocRpt TransTyp=\"0\" RptID=\"%s\" GrpID=\"1234567\" AvgPxGrpID=\"AP101\" Stat=\"3\" BizDt=\"2016-06-03\" RptTyp=\"2\" Qty=\"200\" AvgPxInd=\"2\" Side=\"1\" TrdTyp=\"0\" TrdSubTyp=\"5\" AvgPx=\"57.5054673\" TrdDt=\"2016-06-03\" RndPx=\"57.51\" GrpQty=\"350\" RemQty=\"150\" InptDev=\"API\"><Hdr SID=\"ICE\" TID=\"GUF\"/><Instrmt ID=\"2000019\" Src=\"2\"/><Pty R=\"1\" Src=\"D\" ID=\"TROF\" /><Pty R=\"22\" Src=\"G\" ID=\"XNAS\" /><Pty R=\"3\" Src=\"D\" ID=\"CUSTUS\" /><Amt Typ=\"CRES\"  Amt=\"10.93\" Ccy=\"EUR\"/><Alloc IndAllocID2=\"2827379\" Qty=\"200\"></Alloc> </AllocRpt></FIXML>";

    static AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId("1234567");
        allocationReport.setSecurityId("2000019");
        allocationReport.setTradeSide(AllocationReport.BUY);
        allocationReport.setQuantity(200);
        allocationReport.setPrice(BigDecimal.valueOf(57.5054673));
        allocationReport.setTradeDate("2016-06-03");
        allocationReport.setMicCode("XNAS");
        allocationReport.setCounterpartyId("CUSTUS");
        allocationReport.setExecutingPartyId("TROF");

        return allocationReport;
    }
}
