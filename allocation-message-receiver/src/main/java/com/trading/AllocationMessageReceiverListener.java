package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
class AllocationMessageReceiverListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageReceiverListener.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final FixmlMessageParser parser = new FixmlMessageParser();

    @JmsListener(destination = "incoming.fixml.allocation.report")
    @SendTo("received.json.allocation.report")
    public String processAllocationReport(String message) throws FixmlParserException {

        return toJson(toAllocationReport(message));
    }

    private static String toJson(AllocationReport allocationReport) throws FixmlParserException {
        try {
            String allocationReportAsJson = OBJECT_MAPPER.writeValueAsString(allocationReport);
            LOG.info("Sending Allocation Report #" + allocationReport.getAllocationId());
            return allocationReportAsJson;
        } catch (JsonProcessingException ex) {
            throw new FixmlParserException(ex);
        }
    }

    private AllocationReport toAllocationReport(String message) throws FixmlParserException {

        return parser.parse(message);
    }
}
