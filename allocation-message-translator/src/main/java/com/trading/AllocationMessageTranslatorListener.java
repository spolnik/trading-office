package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import static com.trading.DomainObjectMapper.objectMapper;

@Component
public class AllocationMessageTranslatorListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageTranslatorListener.class);

    private final FixmlMessageParser parser = new FixmlMessageParser();

    @JmsListener(destination = Queues.INCOMING_FIXML_ALLOCATION_REPORT_QUEUE, containerFactory = "jmsContainerFactory")
    @SendTo(Queues.RECEIVED_JSON_ALLOCATION_REPORT_QUEUE)
    public String processAllocationReport(String message) throws FixmlParserException {

        LOG.info("Received: " + message);
        return toJson(toAllocationReport(message));
    }

    private static String toJson(AllocationReport allocationReport) throws FixmlParserException {
        try {
            String allocationReportAsJson = objectMapper().toJson(allocationReport);
            LOG.info("Sending: " + allocationReportAsJson);
            return allocationReportAsJson;
        } catch (JsonProcessingException ex) {
            throw new FixmlParserException(ex);
        }
    }

    private AllocationReport toAllocationReport(String message) throws FixmlParserException {
        AllocationReport allocationReport = parser.parse(message);
        LOG.info("Parsed to: " + allocationReport);

        return allocationReport;
    }
}
