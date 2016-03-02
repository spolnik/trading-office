package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

class CounterpartyApiClient implements CounterpartyApi {

    private static final Logger LOG = LoggerFactory.getLogger(CounterpartyApiClient.class);

    private final RestTemplate restTemplate;

    public CounterpartyApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Exchange getExchange(String micCode) {
        String url = String.format("http://COUNTERPARTY-SERVICE/api/exchange/mic/%s", micCode);
        LOG.info("Getting exchange from: " + url);

        return restTemplate.getForObject(url, Exchange.class);
    }

    @Override
    public String getPartyName(String id) {
        String url = String.format("http://COUNTERPARTY-SERVICE/api/party/%s", id);
        LOG.info("Getting party from: " + url);

        return restTemplate.getForObject(url, String.class);
    }
}
