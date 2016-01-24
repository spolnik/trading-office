package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jaxen.JaxenException;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AllocationMessageTranslatorListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageTranslatorListener.class);

    private final FixmlMessageParser parser = new FixmlMessageParser();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @JmsListener(destination = "front.office.mailbox", containerFactory = "jmsContainerFactory")
    @SendTo("incoming.allocation.report.queue")
    public String processAllocationReport(String message) throws JDOMException, IOException, JaxenException {

        LOG.info("Received: " + message);

        AllocationReport allocationReport = parse(message);
        checkIfSupported(allocationReport);

        return toJson(allocationReport);
    }

    private String toJson(AllocationReport allocationReport) throws JsonProcessingException {
        String allocationReportAsJson = objectMapper.writeValueAsString(allocationReport);
        LOG.info("Sending: " + allocationReportAsJson);
        return allocationReportAsJson;
    }

    private AllocationReport parse(String message) throws JDOMException, IOException, JaxenException {
        AllocationReport allocationReport = parser.parse(message);
        LOG.info("Parsed to: " + allocationReport);
        return allocationReport;
    }

    private void checkIfSupported(AllocationReport allocationReport) {

        hasToBeNewTransaction(allocationReport);
        onlySedolSecurityIdSupported(allocationReport);
    }

    private void onlySedolSecurityIdSupported(AllocationReport allocationReport) {
        if (allocationReport.getSecurityIdSource() != SecurityIDSource.SEDOL) {
            throw new UnsupportedOperationException(
                    "Only SEDOL Security ID source is supported, Message Security ID source: "
                            + allocationReport.getSecurityIdSource()
            );
        }
    }

    private void hasToBeNewTransaction(AllocationReport allocationReport) {
        if (allocationReport.getTransactionType() != TransactionType.NEW) {
            throw new UnsupportedOperationException(
                    "Only NEW transactions can be consumed by Allocation Message Translator, Transaction Type: "
                            + allocationReport.getTransactionType()
            );
        }
    }
}
