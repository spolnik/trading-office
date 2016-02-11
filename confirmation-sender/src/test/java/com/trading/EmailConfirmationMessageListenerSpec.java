package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class EmailConfirmationMessageListenerSpec {

    private EmailConfirmationMessageListener listener;

    private ConfirmationParser confirmationParser;
    private ConfirmationSender confirmationSender;

    @Before
    public void setUp() throws Exception {
        confirmationSender = mock(ConfirmationSender.class);
        confirmationParser = mock(ConfirmationParser.class);

        when(confirmationParser.parse(any())).thenReturn(Optional.of(new Confirmation()));

        listener = new EmailConfirmationMessageListener(
                confirmationSender, confirmationParser
        );
    }

    @Test
    public void uses_confirmation_parser_to_parse_incoming_message() throws Exception {

        listener.onMessage(allocationReportAsJson());
        verify(confirmationParser).parse(any(AllocationReport.class));
    }

    @Test
    public void uses_confirmation_sender_to_send_final_confirmation() throws Exception {

        listener.onMessage(allocationReportAsJson());
        verify(confirmationSender).send(any(Confirmation.class));
    }

    private String allocationReportAsJson() throws JsonProcessingException {
        return objectMapper().toJson(TestData.allocationReport(UUID.randomUUID().toString()));
    }
}