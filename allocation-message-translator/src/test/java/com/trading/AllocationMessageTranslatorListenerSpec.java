package com.trading;

import org.junit.Before;
import org.junit.Test;

public class AllocationMessageTranslatorListenerSpec {

    private static final String TRANSACTION_TYPE_NEW = "0";
    private static final String TRANSACTION_TYPE_REPLACE = "1";
    private static final String TRANSACTION_TYPE_CANCEL = "2";

    private static final String SECURITY_SOURCE_ID_CUSIP = "1";
    private static final String SECURITY_SOURCE_ID_SEDOL = "2";

    public static final String DUMMY_ID = "DUMMY_ID";

    private AllocationMessageTranslatorListener listener;

    @Before
    public void setUp() throws Exception {
        listener = new AllocationMessageTranslatorListener();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void rejects_replace_transactions_as_unsupported() throws Exception {
        String allocationReportMessageWithReplaceTransaction =
                fixmlAllocationReportMessage(DUMMY_ID, TRANSACTION_TYPE_REPLACE, SECURITY_SOURCE_ID_SEDOL);

        listener.processAllocationReport(allocationReportMessageWithReplaceTransaction);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void rejects_cancel_transactions_as_unsupported() throws Exception {
        String allocationReportMessageWithCancelTransaction =
                fixmlAllocationReportMessage(DUMMY_ID, TRANSACTION_TYPE_CANCEL, SECURITY_SOURCE_ID_SEDOL);

        listener.processAllocationReport(allocationReportMessageWithCancelTransaction);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void rejects_new_transaction_with_different_than_sedol_security_id_source() throws Exception {
        String allocationReportMessageWithCusipSecurityId =
                fixmlAllocationReportMessage(DUMMY_ID, TRANSACTION_TYPE_NEW, SECURITY_SOURCE_ID_CUSIP);

        listener.processAllocationReport(allocationReportMessageWithCusipSecurityId);
    }

    private static String fixmlAllocationReportMessage(String id, String transactionType, String securitySourceId) {
        return String.format(
                "<FIXML><AllocRpt TransTyp=\"%s\" RptID=\"%s\" GrpID=\"1234567\" AvgPxGrpID=\"AP101\" Stat=\"3\" BizDt=\"2016-06-03\" RptTyp=\"2\" Qty=\"200\" AvgPxInd=\"2\" Side=\"1\" TrdTyp=\"0\" TrdSubTyp=\"5\" AvgPx=\"57.5054673\" TrdDt=\"2016-06-03\" RndPx=\"57.51\" GrpQty=\"350\" RemQty=\"150\" InptDev=\"API\"><Hdr SID=\"ICE\" TID=\"GUF\"/><Instrmt ID=\"2000019\" Src=\"%s\"/><Pty R=\"22\" ID=\"IFEU\"/><Pty R=\"21\" ID=\"ICEU\"/><Pty R=\"1\" ID=\"GUF\"/><Pty R=\"12\" ID=\"RYN\"/><Pty R=\"4\" ID=\"GUC\"/><Pty R=\"24\" ID=\"CU120978\"/><Pty R=\"38\" ID=\"S\"><Sub ID=\"1\" Typ=\"26\"/></Pty><Amt Typ=\"CRES\"  Amt=\"10.93\" Ccy=\"EUR\"/><Alloc IndAllocID2=\"2827379\" Qty=\"200\"><Pty R=\"22\" ID=\"IFEU\"/> <Pty R=\"21\" ID=\"ICEU\"/> <Pty R=\"1\" ID=\"TUF\"/> <Pty R=\"4\" ID=\"TCF\"/> <Pty R=\"12\" ID=\"TUF\"/> </Alloc> </AllocRpt></FIXML>",
                transactionType, id, securitySourceId
        );
    }
}