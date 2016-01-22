package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jaxen.JaxenException;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TradeMessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(TradeMessageReceiver.class);

    private final FixmlMessageParser parser = new FixmlMessageParser();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ConfigurableApplicationContext context;

    @JmsListener(destination = "front.office.mailbox", containerFactory = "jmsContainerFactory")
    @SendTo("incoming.allocation.report.queue")
    public String processAllocationReport(String message) throws JDOMException, IOException, JaxenException {
        AllocationReport allocationReport = parser.parse(message);
        LOG.info("Received: " + allocationReport);

        String allocationReportAsJson = objectMapper.writeValueAsString(allocationReport);
        LOG.info("Sending: " + allocationReportAsJson);
        return allocationReportAsJson;
    }
}
