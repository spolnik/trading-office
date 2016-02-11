package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

class ConfirmationServiceClient implements ConfirmationSender {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationServiceClient.class);

    private final String confirmationServiceUrl;

    public ConfirmationServiceClient(String confirmationServiceUrl) {
        this.confirmationServiceUrl = confirmationServiceUrl;
    }

    @Override
    public void send(Confirmation confirmation) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(confirmationServiceUrl, confirmation, Object.class);

        LOG.info("Confirmation sent: " + confirmation);
    }
}
