package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class AllocationMessageEnrichmentListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageEnrichmentListener.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${instrumentServiceUrl}")
    private String instrumentServiceUrl;

    @Value("${financeDataServiceUrl}")
    private String financeDataServiceUrl;

    @JmsListener(destination = "incoming.allocation.report.queue", containerFactory = "jmsContainerFactory")
    @SendTo("outgoing.allocation.report.queue")
    public String processAllocationReport(String message) throws IOException {

        try {
            AllocationReport allocationReport = objectMapper.readValue(message, AllocationReport.class);

            LOG.info("Received: " + allocationReport);

            enrich(allocationReport);

            String allocationReportAsJson = objectMapper.writeValueAsString(allocationReport);
            LOG.info("Sending: " + allocationReportAsJson);

            return allocationReportAsJson;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void enrich(AllocationReport allocationReport) {

        InstrumentDetails instrumentDetails = getInstrumentDetails(allocationReport.getSecurityId(), allocationReport.getSecurityIdSource());

        Instrument instrument = getInstrument(instrumentDetails.getTicker());
        allocationReport.setInstrument(instrument);
    }

    private Instrument getInstrument(String ticker) {
        String url = String.format("%s/%s", instrumentServiceUrl, ticker);
        LOG.info("Getting instrument from: " + url);

        return restTemplate.getForObject(url, Instrument.class);
    }

    private InstrumentDetails getInstrumentDetails(String securityId, SecurityIDSource securityIdSource) {

        if (securityIdSource != SecurityIDSource.SEDOL) {
            throw new UnsupportedOperationException("Only SEDOL security id is supported");
        }

        String url = String.format("%s/sedol/%s", financeDataServiceUrl, securityId);
        LOG.info("Getting instrument details from: " + url);

        return restTemplate.getForObject(url, InstrumentDetails.class);
    }
}
