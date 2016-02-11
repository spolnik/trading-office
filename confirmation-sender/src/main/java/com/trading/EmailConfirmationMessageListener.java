package com.trading;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.io.IOException;
import java.util.Optional;

import static com.trading.DomainObjectMapper.objectMapper;

class EmailConfirmationMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(EmailConfirmationMessageListener.class);

    private final ConfirmationSender confirmationSender;
    private final ConfirmationParser confirmationParser;

    public EmailConfirmationMessageListener(
            ConfirmationSender confirmationSender,
            ConfirmationParser confirmationParser
    ) throws JRException {

        this.confirmationSender = confirmationSender;
        this.confirmationParser = confirmationParser;
    }

    @JmsListener(destination = Queues.ENRICHED_JSON_ALLOCATION_REPORT_EMAIL_QUEUE)
    public void onMessage(String message) throws IOException {
        AllocationReport allocationReport = objectMapper().toAllocationReport(message);
        LOG.info("Received: " + allocationReport);

        Optional<Confirmation> confirmation = confirmationParser.parse(allocationReport);

        if (confirmation.isPresent()) {
            confirmationSender.send(confirmation.get());
        } else {
            LOG.error("Cannot create confirmation from: " + message);
        }
    }
}
