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
class AllocationMessageEnrichmentListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageEnrichmentListener.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AllocationReportEnricher enricher;

    @Autowired
    public AllocationMessageEnrichmentListener(AllocationReportEnricher enricher) {
        this.enricher = enricher;
    }

    @JmsListener(destination = "received.json.allocation.report")
    @SendTo("enriched.json.allocation.report")
    public String processAllocationReport(String message) throws IOException {

        AllocationReport allocationReport = fromJson(message);
        LOG.info("Received: " + allocationReport.getAllocationId());
        AllocationReport enrichedAllocationReport = enricher.process(allocationReport);

        return toJson(enrichedAllocationReport);
    }

    private static String toJson(AllocationReport allocationReport) throws JsonProcessingException {
        String allocationReportAsJson = OBJECT_MAPPER.writeValueAsString(allocationReport);
        LOG.info("Sending: " + allocationReportAsJson);
        return allocationReportAsJson;
    }

    private static AllocationReport fromJson(String message) throws IOException {
        AllocationReport allocationReport = OBJECT_MAPPER.readValue(message, AllocationReport.class);
        LOG.info("Received: " + allocationReport);
        return allocationReport;
    }
}
