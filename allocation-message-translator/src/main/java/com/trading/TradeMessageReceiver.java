package com.trading;

import org.jaxen.JaxenException;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TradeMessageReceiver {

    private static Logger log = LoggerFactory.getLogger(TradeMessageReceiver.class);
    private final FixmlMessageParser parser = new FixmlMessageParser();


    @Autowired
    private ConfigurableApplicationContext context;

    @JmsListener(destination = "front-office-mailbox", containerFactory = "jmsContainerFactory")
    public void receiveMessage(String message) throws JDOMException, IOException, JaxenException {
        AllocationReport allocationReport = parser.parse(message);
        log.info("Received <" + allocationReport + ">");
        context.close();
    }
}
