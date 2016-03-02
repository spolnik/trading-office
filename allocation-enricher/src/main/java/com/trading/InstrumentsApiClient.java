package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

class InstrumentsApiClient implements InstrumentsApi {

    private static final Logger LOG = LoggerFactory.getLogger(InstrumentsApiClient.class);

    private final RestTemplate restTemplate;
    private final String host;

    public InstrumentsApiClient(RestTemplate restTemplate, String host) {
        this.restTemplate = restTemplate;
        this.host = host;
    }

    @Override
    public InstrumentDetails getInstrumentDetails(String securityId) {

        String url = String.format("http://%s/api/instruments/sedol/%s", host, securityId);
        LOG.info("Getting instrument details from: " + url);

        return restTemplate.getForObject(url, InstrumentDetails.class);
    }

    @Override
    public Instrument getInstrument(String ticker) {
        String url = String.format("http://%s/api/instrument/%s", host, ticker);
        LOG.info("Getting instrument from: " + url);

        return restTemplate.getForObject(url, Instrument.class);
    }
}
