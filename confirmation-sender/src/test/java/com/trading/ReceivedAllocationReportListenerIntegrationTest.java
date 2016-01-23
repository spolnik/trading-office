package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReceivedAllocationReportListenerIntegrationTest {

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

        ObjectMapper objectMapper = new ObjectMapper();
        String allocationReportAsJson = objectMapper.writeValueAsString(TestData.allocationReport());

        EnrichedAllocationReportMessageListener enrichedAllocationReportMessageListener = new EnrichedAllocationReportMessageListener(confirmationSender);
        enrichedAllocationReportMessageListener.eventListener(allocationReportAsJson);
        verify(confirmationSender).send(argument.capture());

        AllocationReport allocationReportWithStatusSent = argument.getValue().getAllocationReport();

        assertThat(allocationReportWithStatusSent).isEqualTo(TestData.allocationReport());
    }
}
