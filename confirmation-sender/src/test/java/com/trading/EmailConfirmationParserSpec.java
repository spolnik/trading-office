package com.trading;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailConfirmationParserSpec {

    private static final String DUMMY_ALLOCATION_ID = UUID.randomUUID().toString();

    private ConfirmationParser confirmationParser;

    @Before
    public void setUp() throws Exception {
        confirmationParser = new EmailConfirmationParser();
    }

    @Test
    public void retrieves_new_message_and_process_it_finally_saving_with_status_sent() throws Exception {

        Confirmation allocationReport = TestData.allocationReport(DUMMY_ALLOCATION_ID);

        Optional<Confirmation> confirmation = confirmationParser.parse(allocationReport);
        Confirmation allocationReportWithStatusSent = confirmation.get();

        assertThat(allocationReportWithStatusSent).isEqualToIgnoringGivenFields(
                TestData.allocationReport(DUMMY_ALLOCATION_ID), "content", "confirmationType"
        );
    }
}
