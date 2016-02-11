package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

class CounterpartyApiClient implements CounterpartyApi {

    private static final Logger LOG = LoggerFactory.getLogger(CounterpartyApiClient.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private String counterpartyServiceUrl;

    public CounterpartyApiClient(String counterpartyServiceUrl) {
        this.counterpartyServiceUrl = counterpartyServiceUrl;
    }

    @Override
    public Exchange getExchange(String micCode) {
        String url = String.format("%s/api/exchange/mic/%s", counterpartyServiceUrl, micCode);
        LOG.info("Getting exchange from: " + url);

        return restTemplate.getForObject(url, Exchange.class);
    }

    @Override
    public Party getParty(String id) {
        String url = String.format("%s/api/party/%s", counterpartyServiceUrl, id);
        LOG.info("Getting party from: " + url);

        return restTemplate.getForObject(url, Party.class);
    }
}
