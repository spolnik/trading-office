package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.io.IOException;
import java.util.Optional;

import static com.trading.DomainObjectMapper.objectMapper;

class ConfirmationMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationMessageListener.class);

    private final ConfirmationSender confirmationSender;
    private final ConfirmationParser emailConfirmationParser;
    private final ConfirmationParser swiftConfirmationParser;

    private final ConfirmationApi confirmationApi;

    public ConfirmationMessageListener(
            ConfirmationSender confirmationSender,
            ConfirmationParser emailConfirmationParser,
            ConfirmationParser swiftConfirmationParser,
            ConfirmationApi confirmationApi
    ) {

        this.confirmationSender = confirmationSender;
        this.emailConfirmationParser = emailConfirmationParser;
        this.swiftConfirmationParser = swiftConfirmationParser;
        this.confirmationApi = confirmationApi;
    }

    @JmsListener(destination = "enriched.json.allocation.report")
    public void onMessage(String message) throws IOException {
        AllocationReport allocationReport = objectMapper().toAllocationReport(message);
        LOG.info("Received: " + allocationReport);

        ConfirmationType confirmationType = confirmationApi.confirmationTypeFor(allocationReport.getExchange().getMic());
        Optional<Confirmation> confirmation = confirmationParserFor(confirmationType).parse(allocationReport);

        confirmationSender.send(confirmation.get());
    }

    private ConfirmationParser confirmationParserFor(ConfirmationType confirmationType) {
        ConfirmationParser confirmationParser;

        if (ConfirmationType.EMAIL.equals(confirmationType)) {
            confirmationParser = emailConfirmationParser;
        } else {
            confirmationParser = swiftConfirmationParser;
        }
        return confirmationParser;
    }
}
