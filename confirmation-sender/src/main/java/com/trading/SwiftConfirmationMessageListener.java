package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.trading.DomainObjectMapper.objectMapper;

@Component
class SwiftConfirmationMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(SwiftConfirmationMessageListener.class);

    private final ConfirmationSender confirmationSender;
    private final ConfirmationParser confirmationParser;

    @Autowired
    public SwiftConfirmationMessageListener(ConfirmationSender confirmationSender, ConfirmationParser confirmationParser) {
        this.confirmationSender = confirmationSender;
        this.confirmationParser = confirmationParser;
    }

    @JmsListener(destination = Queues.ENRICHED_JSON_ALLOCATION_REPORT_SWIFT_QUEUE, containerFactory = "jmsContainerFactory")
    public void onMessage(String message) throws IOException {
        AllocationReport allocationReport = objectMapper().toAllocationReport(message);
        LOG.info("Received: " + allocationReport);

        Optional<Confirmation> confirmation = confirmationParser.parse(allocationReport);

        confirmationSender.send(confirmation.get());
    }
}
