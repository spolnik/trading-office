package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestCounterpartyApi implements CounterpartyApi {

    private static final Logger LOG = LoggerFactory.getLogger(RestCounterpartyApi.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${counterpartyServiceUrl}")
    private String counterpartyServiceUrl;

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
