package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

class ConfirmationServiceClient implements ConfirmationSender {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationServiceClient.class);

    private final RestTemplate restTemplate;
    private final String host;

    public ConfirmationServiceClient(RestTemplate restTemplate, String host) {
        this.restTemplate = restTemplate;
        this.host = host;
    }

    @Override
    public void send(Confirmation confirmation) {

        String url = String.format("http://%s/api/confirmation", host);
        restTemplate.postForObject(url, confirmation, Object.class);

        LOG.info("Confirmation sent: " + confirmation);
    }
}
