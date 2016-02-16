package com.trading;

import org.junit.Before;
import org.junit.Test;

public class AllocationMessageReceiverListenerSpec {

    private static final String TRANSACTION_TYPE_NEW = "0";
    private static final String TRANSACTION_TYPE_REPLACE = "1";
    private static final String TRANSACTION_TYPE_CANCEL = "2";

    private static final String SECURITY_SOURCE_ID_CUSIP = "1";
    private static final String SECURITY_SOURCE_ID_SEDOL = "2";

    private AllocationMessageReceiverListener listener;

    @Before
    public void setUp() throws Exception {
        listener = new AllocationMessageReceiverListener();
    }

    @Test(expected = FixmlParserException.class)
    public void rejects_replace_transactions_as_unsupported() throws Exception {
        String allocationReportMessageWithReplaceTransaction =
                fixmlAllocationReportMessage(TRANSACTION_TYPE_REPLACE, SECURITY_SOURCE_ID_SEDOL);

        listener.processAllocationReport(allocationReportMessageWithReplaceTransaction);
    }

    @Test(expected = FixmlParserException.class)
    public void rejects_cancel_transactions_as_unsupported() throws Exception {
        String allocationReportMessageWithCancelTransaction =
                fixmlAllocationReportMessage(TRANSACTION_TYPE_CANCEL, SECURITY_SOURCE_ID_SEDOL);

        listener.processAllocationReport(allocationReportMessageWithCancelTransaction);
    }

    @Test(expected = FixmlParserException.class)
    public void rejects_new_transaction_with_different_than_sedol_security_id_source() throws Exception {
        String allocationReportMessageWithCusipSecurityId =
                fixmlAllocationReportMessage(TRANSACTION_TYPE_NEW, SECURITY_SOURCE_ID_CUSIP);

        listener.processAllocationReport(allocationReportMessageWithCusipSecurityId);
    }

    @Test(expected = FixmlParserException.class)
    public void throws_exception_if_incorrect_json_format() throws Exception {
        listener.processAllocationReport("BROKEN");
    }

    private static String fixmlAllocationReportMessage(String transactionType, String securitySourceId) {
        return String.format(
                "<FIXML><AllocRpt TransTyp=\"%s\" RptID=\"DUMMY_ID\" GrpID=\"1234567\" AvgPxGrpID=\"AP101\" Stat=\"3\" BizDt=\"2016-06-03\" RptTyp=\"2\" Qty=\"200\" AvgPxInd=\"2\" Side=\"1\" TrdTyp=\"0\" TrdSubTyp=\"5\" AvgPx=\"57.5054673\" TrdDt=\"2016-06-03\" RndPx=\"57.51\" GrpQty=\"350\" RemQty=\"150\" InptDev=\"API\"><Hdr SID=\"ICE\" TID=\"GUF\"/><Instrmt ID=\"2000019\" Src=\"%s\"/><Pty R=\"1\" Src=\"D\" ID=\"TROF\" /><Pty R=\"22\" Src=\"G\" ID=\"XNAS\" /><Pty R=\"3\" Src=\"D\" ID=\"CUSTUS\" /><Amt Typ=\"CRES\"  Amt=\"10.93\" Ccy=\"EUR\"/><Alloc IndAllocID2=\"2827379\" Qty=\"200\"></Alloc> </AllocRpt></FIXML>",
                transactionType, securitySourceId
        );
    }
}