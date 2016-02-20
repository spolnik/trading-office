package com.trading;

import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FixmlMessageParserSpec {

    private static final String FIXML_ALLOCATION_REPORT_MESSAGE = "<FIXML><AllocRpt TransTyp=\"0\" RptID=\"%s\" GrpID=\"1234567\" AvgPxGrpID=\"AP101\" Stat=\"3\" BizDt=\"2016-06-03\" RptTyp=\"2\" Qty=\"200\" AvgPxInd=\"2\" Side=\"%s\" TrdTyp=\"0\" TrdSubTyp=\"5\" AvgPx=\"57.5054673\" TrdDt=\"2016-06-03\" RndPx=\"57.51\" GrpQty=\"350\" RemQty=\"150\" InptDev=\"API\"><Hdr SID=\"ICE\" TID=\"GUF\"/><Instrmt ID=\"2000019\" Src=\"2\"/><Pty R=\"1\" Src=\"D\" ID=\"TROF\" /><Pty R=\"22\" Src=\"G\" ID=\"XNAS\" /><Pty R=\"3\" Src=\"D\" ID=\"CUSTUS\" /><Amt Typ=\"CRES\"  Amt=\"10.93\" Ccy=\"EUR\"/><Alloc IndAllocID2=\"2827379\" Qty=\"200\"></Alloc> </AllocRpt></FIXML>";

    private static FixmlMessageParser parser;
    private static AllocationReport allocationReport;

    private static final String ALLOCATION_REPORT_ID = UUID.randomUUID().toString();
    private static final String BUY_SIDE = "1";
    private static final String SELL_SIDE = "2";

    @BeforeClass
    public static void setUp() throws Exception {
        parser = new FixmlMessageParser();
        allocationReport = parser.parse(
                String.format(FIXML_ALLOCATION_REPORT_MESSAGE, ALLOCATION_REPORT_ID, BUY_SIDE)
        );
    }

    @Test(expected = FixmlParserException.class)
    public void throws_exception_if_incorrect_json_format() throws Exception {
        parser.parse("BROKEN");
    }

    @Test
    public void parses_allocation_id() throws Exception {
        assertThat(allocationReport.getAllocationId()).isEqualTo(ALLOCATION_REPORT_ID);
    }

    @Test
    public void parses_security_id() throws Exception {
        assertThat(allocationReport.getSecurityId()).isEqualTo("2000019");
    }

    @Test
    public void parses_transaction_buy_side() throws Exception {
        assertThat(allocationReport.getTradeSide()).isEqualTo(AllocationReport.BUY);
    }

    @Test
    public void parses_transaction_sell_side() throws Exception {
        allocationReport = parser.parse(
                String.format(FIXML_ALLOCATION_REPORT_MESSAGE, ALLOCATION_REPORT_ID, SELL_SIDE)
        );
        assertThat(allocationReport.getTradeSide()).isEqualTo(AllocationReport.SELL);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_exception_if_unsupported_side() throws Exception {
        String unsupportedSide = "3";

        parser.parse(
                String.format(FIXML_ALLOCATION_REPORT_MESSAGE, ALLOCATION_REPORT_ID, unsupportedSide)
        );
    }

    @Test
    public void parses_quantity() throws Exception {
        assertThat(allocationReport.getQuantity()).isEqualTo(200);
    }

    @Test
    public void parses_price() throws Exception {
        assertThat(allocationReport.getPrice()).isEqualTo(BigDecimal.valueOf(57.5054673));
    }

    @Test
    public void parses_trade_date() throws Exception {
        assertThat(allocationReport.getTradeDate()).isEqualTo("2016-06-03");
    }

    @Test
    public void parses_exchange_mic_code() throws Exception {
        assertThat(allocationReport.getMicCode()).isEqualTo(
                "XNAS"
        );
    }

    @Test
    public void parses_counterparty_id() throws Exception {
        assertThat(allocationReport.getCounterpartyId()).isEqualTo(
                "CUSTUS"
        );
    }

    @Test
    public void parses_executing_party_id() throws Exception {
        assertThat(allocationReport.getExecutingPartyId()).isEqualTo(
                "TROF"
        );
    }
}
