package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

class ConfirmationServiceClient implements ConfirmationSender {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationServiceClient.class);

    private final String confirmationServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ConfirmationServiceClient(String confirmationServiceUrl) {
        this.confirmationServiceUrl = confirmationServiceUrl;
    }

    @Override
    public void send(Confirmation confirmation) {

        String url = String.format("%s/api/confirmation", confirmationServiceUrl);
        restTemplate.postForObject(url, confirmation, Object.class);

        LOG.info("Confirmation sent: " + confirmation);
    }
}
