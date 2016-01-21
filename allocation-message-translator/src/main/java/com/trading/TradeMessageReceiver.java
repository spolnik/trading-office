package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jaxen.JaxenException;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.Message;
import java.io.IOException;

@Component
public class TradeMessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(TradeMessageReceiver.class);

    private final FixmlMessageParser parser = new FixmlMessageParser();
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    public TradeMessageReceiver(ConnectionFactory connectionFactory) {
        jmsTemplate = new JmsTemplate(connectionFactory);
        objectMapper = new ObjectMapper();
    }

    @JmsListener(destination = "front.office.mailbox", containerFactory = "jmsContainerFactory")
    public void receiveMessage(String message) throws JDOMException, IOException, JaxenException {
        AllocationReport allocationReport = parser.parse(message);
        LOG.info("Received: " + allocationReport);

        String allocationReportAsJson = objectMapper.writeValueAsString(allocationReport);

        jmsTemplate.send(
                "incoming.allocation.report.queue",
                session -> {
                    Message jsonMessage = session.createTextMessage(allocationReportAsJson);
                    LOG.info("Sending: " + jsonMessage);
                    return jsonMessage;
                }
        );
    }
}
