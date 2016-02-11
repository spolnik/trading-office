package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InstrumentsApiClient implements InstrumentsApi {

    private static final Logger LOG = LoggerFactory.getLogger(InstrumentsApiClient.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${instrumentServiceUrl}")
    private String instrumentServiceUrl;

    @Value("${financeDataServiceUrl}")
    private String financeDataServiceUrl;

    @Override
    public InstrumentDetails getInstrumentDetails(String securityId, InstrumentType instrumentType) {

        String url = String.format("%s/api/instruments/sedol/%s", instrumentServiceUrl, securityId);
        LOG.info("Getting instrument details from: " + url);

        return restTemplate.getForObject(url, InstrumentDetails.class);
    }

    @Override
    public Instrument getInstrument(String ticker) {
        String url = String.format("%s/api/instrument/%s", financeDataServiceUrl, ticker);
        LOG.info("Getting instrument from: " + url);

        return restTemplate.getForObject(url, Instrument.class);
    }
}
