package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReceivedAllocationReportListenerIntegrationTest {

    private ConfirmationSender confirmationSender;
    private ArgumentCaptor<Confirmation> argument;

    private static final String DUMMY_ALLOCATION_ID = UUID.randomUUID().toString();

    @Before
    public void setUp() throws Exception {
        confirmationSender = mock(ConfirmationSender.class);

        argument = ArgumentCaptor.forClass(
                Confirmation.class
        );
    }

    @Test
    public void retrieves_new_message_and_process_it_finally_saving_with_status_sent() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String allocationReportAsJson = objectMapper.writeValueAsString(
                TestData.allocationReport(DUMMY_ALLOCATION_ID)
        );

        EnrichedAllocationReportMessageListener enrichedAllocationReportMessageListener = new EnrichedAllocationReportMessageListener(confirmationSender);
        enrichedAllocationReportMessageListener.eventListener(allocationReportAsJson);
        verify(confirmationSender).send(argument.capture());

        AllocationReport allocationReportWithStatusSent = argument.getValue().getAllocationReport();

        AllocationReport expected = TestData.allocationReport(DUMMY_ALLOCATION_ID);
        expected.setMessageStatus(MessageStatus.SENT);

        assertThat(allocationReportWithStatusSent).isEqualToComparingFieldByField(expected);
    }
}
