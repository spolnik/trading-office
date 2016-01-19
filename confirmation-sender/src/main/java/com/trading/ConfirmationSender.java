package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConfirmationSender implements Sender<Confirmation> {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationSender.class);

    @Value("${confirmationServiceUrl}")
    private String confirmationServiceUrl;

    @Override
    public void send(Confirmation confirmation) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(confirmationServiceUrl, confirmation, Object.class);

        LOG.info("Confirmation sent: " + confirmation);
    }
}
