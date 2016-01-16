package com.trading;

import org.junit.Before;
import org.junit.Test;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

public class ReceivedAllocationReportListenerIntegrationTest {

    private static final String ALLOCATION_REPORT_ID = "1234";

    private GigaSpace gigaSpace;

    @Before
    public void setUp() throws Exception {
        gigaSpace = new GigaSpaceConfigurer(
                new UrlSpaceConfigurer("/./test")
        ).gigaSpace();
    }

    @Test
    public void can_save_and_retrieve_allocation_report_pojo() throws Exception {

        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setAllocationId(ALLOCATION_REPORT_ID);
        allocationReport.setTradeType("BUY");
        allocationReport.setStatus(MessageStatus.NEW);

        gigaSpace.write(allocationReport);
        makeSureSpaceCountIs(1);

        AllocationReport allocationReportWithStatusNew = gigaSpace.takeById(AllocationReport.class, ALLOCATION_REPORT_ID);
        makeSureSpaceCountIs(0);

        ReceivedAllocationReportListener receivedAllocationReportListener = new ReceivedAllocationReportListener();
        receivedAllocationReportListener.eventListener(allocationReportWithStatusNew, gigaSpace);

        AllocationReport allocationReportWithStatusSent = gigaSpace.takeById(AllocationReport.class, ALLOCATION_REPORT_ID);
        makeSureSpaceCountIs(0);

        assertThat(allocationReportWithStatusSent.getAllocationId()).isEqualTo(ALLOCATION_REPORT_ID);
        assertThat(allocationReportWithStatusSent.getTradeType()).isEqualTo("BUY");
        assertThat(allocationReportWithStatusSent.getStatus()).isEqualTo(MessageStatus.SENT);
    }

    private void makeSureSpaceCountIs(int expected) {
        assertThat(gigaSpace.count(new AllocationReport())).isEqualTo(expected);
    }
}
