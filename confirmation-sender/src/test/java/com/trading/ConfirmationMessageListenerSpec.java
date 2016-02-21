package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class ConfirmationMessageListenerSpec {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ConfirmationMessageListener listener;

    private ConfirmationSender confirmationSender;
    private ConfirmationParser emailConfirmationParser;
    private ConfirmationParser swiftConfirmationParser;

    @Before
    public void setUp() throws Exception {
        confirmationSender = mock(ConfirmationSender.class);
        emailConfirmationParser = mock(ConfirmationParser.class);
        swiftConfirmationParser = mock(ConfirmationParser.class);

        setupParser(emailConfirmationParser);
        setupParser(swiftConfirmationParser);

        listener = new ConfirmationMessageListener(
                confirmationSender,
                emailConfirmationParser,
                swiftConfirmationParser
        );
    }

    @Test
    public void uses_email_confirmation_parser_to_parse_incoming_message_with_email_confirmation_type() throws Exception {

        listener.onMessage(allocationReportAsJson());
        verify(emailConfirmationParser).parse(any(Confirmation.class));
    }

    @Test
    public void uses_swift_confirmation_parser_to_parse_incoming_message_with_london_stock_exchange_mic_code() throws Exception {

        listener.onMessage(allocationReportAsJson("XLON"));
        verify(swiftConfirmationParser).parse(any(Confirmation.class));
    }

    @Test
    public void uses_confirmation_sender_to_send_final_confirmation() throws Exception {

        listener.onMessage(allocationReportAsJson());
        verify(confirmationSender).send(any(Confirmation.class));
    }

    private String allocationReportAsJson() throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(TestData.confirmation(UUID.randomUUID().toString()));
    }

    private String allocationReportAsJson(String micCode) throws JsonProcessingException {

        Confirmation allocationReport = TestData.confirmation(UUID.randomUUID().toString());
        allocationReport.setMicCode(micCode);

        return OBJECT_MAPPER.writeValueAsString(allocationReport);
    }

    private void setupParser(ConfirmationParser parser) {
        when(parser.parse(any())).thenReturn(Optional.of(new Confirmation()));
    }
}