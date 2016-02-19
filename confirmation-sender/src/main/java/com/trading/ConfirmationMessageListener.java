package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.io.IOException;
import java.util.Optional;

class ConfirmationMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationMessageListener.class);

    private static final String LONDON_STOCK_EXCHANGE_MIC_CODE = "XLON";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ConfirmationSender confirmationSender;
    private final ConfirmationParser emailConfirmationParser;

    private final ConfirmationParser swiftConfirmationParser;

    public ConfirmationMessageListener(
            ConfirmationSender confirmationSender,
            ConfirmationParser emailConfirmationParser,
            ConfirmationParser swiftConfirmationParser
    ) {

        this.confirmationSender = confirmationSender;
        this.emailConfirmationParser = emailConfirmationParser;
        this.swiftConfirmationParser = swiftConfirmationParser;
    }

    @JmsListener(destination = "enriched.json.allocation.report")
    public void onMessage(String message) throws IOException {
        AllocationReport allocationReport = OBJECT_MAPPER.readValue(message, AllocationReport.class);
        LOG.info("Received: " + allocationReport);

        Optional<Confirmation> confirmation = confirmationParserFor(allocationReport.getExchange().getMic()).parse(allocationReport);

        confirmationSender.send(confirmation.get());
    }

    private ConfirmationParser confirmationParserFor(String micCode) {

        return LONDON_STOCK_EXCHANGE_MIC_CODE.equals(micCode)
                ? swiftConfirmationParser
                : emailConfirmationParser;
    }
}
