package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
class AllocationMessageReceiverListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageReceiverListener.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final FixmlMessageParser parser;

    @Autowired
    public AllocationMessageReceiverListener(FixmlMessageParser parser) {
        this.parser = parser;
    }

    @JmsListener(destination = "incoming.fixml.allocation.report")
    @SendTo("received.json.allocation.report")
    public String processAllocationReport(String message) throws FixmlParserException, JsonProcessingException { // NOSONAR

        return toJson(toAllocationReport(message));
    }

    private static String toJson(Allocation allocation) throws FixmlParserException, JsonProcessingException {
        LOG.info("Received: " + allocation.getAllocationId());

        String allocationReportAsJson = OBJECT_MAPPER.writeValueAsString(allocation);
        LOG.info("Sending Allocation Report: " + allocation.getAllocationId());
        return allocationReportAsJson;

    }

    private Allocation toAllocationReport(String message) throws FixmlParserException {

        return parser.parse(message);
    }
}
