package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.trading.DomainObjectMapper.objectMapper;

@Component
public class AllocationMessageEnrichmentListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageEnrichmentListener.class);

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

    private static String toJson(AllocationReport allocationReport) throws JsonProcessingException {
        String allocationReportAsJson = objectMapper().toJson(allocationReport);
        LOG.info("Sending: " + allocationReportAsJson);
        return allocationReportAsJson;
    }

    private static AllocationReport fromJson(String message) throws IOException {
        AllocationReport allocationReport = objectMapper().toAllocationReport(message);
        LOG.info("Received: " + allocationReport);
        return allocationReport;
    }

    private AllocationReport enrich(AllocationReport allocationReport) {

        InstrumentDetails instrumentDetails = instrumentsApi.getInstrumentDetails(
                allocationReport.getSecurityId(), allocationReport.getInstrumentType()
        );

        Instrument instrument = instrumentsApi.getInstrument(instrumentDetails.getTicker());
        allocationReport.setInstrument(instrument);

        return allocationReport;
    }
}
