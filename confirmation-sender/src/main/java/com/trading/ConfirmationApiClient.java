package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class ConfirmationApiClient implements ConfirmationApi {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationApiClient.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final String confirmationServiceUrl;

    public ConfirmationApiClient(String confirmationServiceUrl) {
        this.confirmationServiceUrl = confirmationServiceUrl;
    }

    @Override
    public ConfirmationType confirmationTypeFor(String micCode) {
        String url = String.format("%s/api/confirmation/type/%s", confirmationServiceUrl, micCode);
        LOG.info("Getting confirmation type from: " + url);

        return restTemplate.getForObject(url, ConfirmationType.class);
    }
}
