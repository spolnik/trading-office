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

        EnrichedAllocation allocation = fromJson(message);
        LOG.info("Received: " + allocation.getAllocationId());
        EnrichedAllocation enrichedAllocation = enricher.process(allocation);

        return toJson(enrichedAllocation);
    }

    private static String toJson(EnrichedAllocation allocation) throws JsonProcessingException {
        String allocationAsJson = OBJECT_MAPPER.writeValueAsString(allocation);
        LOG.info("Sending: " + allocationAsJson);
        return allocationAsJson;
    }

    private static EnrichedAllocation fromJson(String message) throws IOException {
        EnrichedAllocation allocation = OBJECT_MAPPER.readValue(message, EnrichedAllocation.class);
        LOG.info("Received: " + allocation);
        return allocation;
    }
}
