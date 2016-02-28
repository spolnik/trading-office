package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class EnrichedAllocationReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(EnrichedAllocationReceiver.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String LONDON_STOCK_EXCHANGE_MIC_CODE = "XLON";

    private final ConfirmationSender confirmationSender;
    private final ConfirmationParser emailConfirmationParser;
    private final ConfirmationParser swiftConfirmationParser;

    public EnrichedAllocationReceiver(ConfirmationSender confirmationSender,
                                      ConfirmationParser emailConfirmationParser,
                                      ConfirmationParser swiftConfirmationParser) {

        this.confirmationSender = confirmationSender;
        this.emailConfirmationParser = emailConfirmationParser;
        this.swiftConfirmationParser = swiftConfirmationParser;
    }

    public void handleMessage(String message) throws IOException {
        Confirmation allocationReport = OBJECT_MAPPER.readValue(message, Confirmation.class);
        LOG.info("Received: " + allocationReport);
        Optional<Confirmation> confirmation = confirmationParserFor(allocationReport.getMicCode()).parse(allocationReport);

        confirmationSender.send(confirmation.get());
    }

    private ConfirmationParser confirmationParserFor(String micCode) {

        return LONDON_STOCK_EXCHANGE_MIC_CODE.equals(micCode)
                ? swiftConfirmationParser
                : emailConfirmationParser;
    }
}
