package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class ConfirmationSender implements Sender<Confirmation> {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationSender.class);

    @Override
    public void send(Confirmation confirmation) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:9000/api/confirmation", confirmation, Object.class);

        LOG.info("Confirmation sent: " + confirmation);
    }
}
