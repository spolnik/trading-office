package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AllocationMessageEnrichmentListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageEnrichmentListener.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InstrumentsApi instrumentsApi;

    @Autowired
    public AllocationMessageEnrichmentListener(InstrumentsApi instrumentsApi) {
        this.instrumentsApi = instrumentsApi;
    }

    @JmsListener(destination = "incoming.allocation.report.queue", containerFactory = "jmsContainerFactory")
    @SendTo("outgoing.allocation.report.queue")
    public String processAllocationReport(String message) throws IOException {

        return toJson(enrich(fromJson(message)));
    }

    private String toJson(AllocationReport allocationReport) throws JsonProcessingException {
        String allocationReportAsJson = objectMapper.writeValueAsString(allocationReport);
        LOG.info("Sending: " + allocationReportAsJson);
        return allocationReportAsJson;
    }

    private AllocationReport fromJson(String message) throws IOException {
        AllocationReport allocationReport = objectMapper.readValue(message, AllocationReport.class);
        LOG.info("Received: " + allocationReport);
        return allocationReport;
    }

    private AllocationReport enrich(AllocationReport allocationReport) {

        InstrumentDetails instrumentDetails = instrumentsApi.getInstrumentDetails(
                allocationReport.getSecurityId(), allocationReport.getSecurityIdSource()
        );

        Instrument instrument = instrumentsApi.getInstrument(instrumentDetails.getTicker());
        allocationReport.setInstrument(instrument);

        return allocationReport;
    }
}
