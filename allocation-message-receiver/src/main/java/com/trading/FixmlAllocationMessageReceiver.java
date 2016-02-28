package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

public class FixmlAllocationMessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(FixmlAllocationMessageReceiver.class);

    private final AmqpTemplate amqpTemplate;
    private final FixmlMessageParser parser;

    public FixmlAllocationMessageReceiver(AmqpTemplate amqpTemplate, FixmlMessageParser parser) {
        this.amqpTemplate = amqpTemplate;
        this.parser = parser;
    }

    public void handleMessage(String message) throws FixmlParserException {
        LOG.info("Received: " + message);

        Allocation allocation = parser.parse(message);
        amqpTemplate.convertAndSend("trading-office-exchange", "received.json.allocation.report", allocation);
    }
}
