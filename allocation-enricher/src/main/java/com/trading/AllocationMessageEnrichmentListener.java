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
class AllocationMessageEnrichmentListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageEnrichmentListener.class);

    private final AllocationReportEnricher enricher;

    @Autowired
    public AllocationMessageEnrichmentListener(AllocationReportEnricher enricher) {
        this.enricher = enricher;
    }

    @JmsListener(destination = Queues.RECEIVED_JSON_ALLOCATION_REPORT_QUEUE)
    @SendTo(Queues.ENRICHED_JSON_ALLOCATION_REPORT_EMAIL_QUEUE)
    public String processAllocationReport(String message) throws IOException {

        AllocationReport allocationReport = fromJson(message);
        AllocationReport enrichedAllocationReport = enricher.process(allocationReport);

        return toJson(enrichedAllocationReport);
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
}
