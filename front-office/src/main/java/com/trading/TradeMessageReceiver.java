package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TradeMessageReceiver {

    private static Logger log = LoggerFactory.getLogger(TradeMessageReceiver.class);

    @Autowired
    private ConfigurableApplicationContext context;

    @JmsListener(destination = "front-office-mailbox", containerFactory = "frontOfficeJmsContainerFactory")
    public void receiveMessage(String message) {
        log.info("Received <" + message + ">");
        context.close();
    }
}
