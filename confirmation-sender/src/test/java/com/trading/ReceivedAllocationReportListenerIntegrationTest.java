package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReceivedAllocationReportListenerIntegrationTest {

    private static final String ALLOCATION_REPORT_ID = "1234";


    private ConfirmationSender confirmationSender;
    private ArgumentCaptor<Confirmation> argument;

    @Before
    public void setUp() throws Exception {
        confirmationSender = mock(ConfirmationSender.class);

        argument = ArgumentCaptor.forClass(
                Confirmation.class
        );
    }

    @Test
    public void retrieves_new_message_and_process_it_finally_saving_with_status_sent() throws Exception {

        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setAllocationId(ALLOCATION_REPORT_ID);
        allocationReport.setTransactionType(TransactionType.NEW);
        allocationReport.setMessageStatus(MessageStatus.NEW);

        ObjectMapper objectMapper = new ObjectMapper();
        String allocationReportAsJson = objectMapper.writeValueAsString(allocationReport);

        EnrichedAllocationReportMessageListener enrichedAllocationReportMessageListener = new EnrichedAllocationReportMessageListener(confirmationSender);
        enrichedAllocationReportMessageListener.eventListener(allocationReportAsJson);
        verify(confirmationSender).send(argument.capture());

        AllocationReport allocationReportWithStatusSent = argument.getValue().getAllocationReport();

        assertThat(allocationReportWithStatusSent.getAllocationId()).isEqualTo(ALLOCATION_REPORT_ID);
        assertThat(allocationReportWithStatusSent.getTransactionType()).isEqualTo(TransactionType.NEW);
        assertThat(allocationReportWithStatusSent.getMessageStatus()).isEqualTo(MessageStatus.SENT);
    }
}
