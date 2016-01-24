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
                TestData.fixmlAllocationReportMessage(DUMMY_ID, TRANSACTION_TYPE_REPLACE, SECURITY_SOURCE_ID_SEDOL);

        listener.processAllocationReport(allocationReportMessageWithReplaceTransaction);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void rejects_cancel_transactions_as_unsupported() throws Exception {
        String allocationReportMessageWithCancelTransaction =
                TestData.fixmlAllocationReportMessage(DUMMY_ID, TRANSACTION_TYPE_CANCEL, SECURITY_SOURCE_ID_SEDOL);

        listener.processAllocationReport(allocationReportMessageWithCancelTransaction);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void rejects_new_transaction_with_different_than_sedol_security_id_source() throws Exception {
        String allocationReportMessageWithCusipSecurityId =
                TestData.fixmlAllocationReportMessage(DUMMY_ID, TRANSACTION_TYPE_NEW, SECURITY_SOURCE_ID_CUSIP);

        listener.processAllocationReport(allocationReportMessageWithCusipSecurityId);
    }
}