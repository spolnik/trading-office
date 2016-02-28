package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

import java.io.IOException;

public class AllocationReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationReceiver.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AmqpTemplate amqpTemplate;
    private final AllocationEnricher allocationEnricher;

    public AllocationReceiver(AmqpTemplate amqpTemplate, AllocationEnricher allocationEnricher) {
        this.amqpTemplate = amqpTemplate;
        this.allocationEnricher = allocationEnricher;
    }

    public void handleMessage(String message) throws IOException {

        EnrichedAllocation allocation = fromJson(message);
        LOG.info("Received: " + allocation.getAllocationId());

        EnrichedAllocation enrichedAllocation = allocationEnricher.process(allocation);
        String enrichedAllocationAsJson = toJson(enrichedAllocation);
        amqpTemplate.convertAndSend("trading-office-exchange", "enriched.json.allocation.report", enrichedAllocationAsJson);
    }

    private static EnrichedAllocation fromJson(String message) throws IOException {
        EnrichedAllocation allocation = OBJECT_MAPPER.readValue(message, EnrichedAllocation.class);
        LOG.info("Received: " + allocation);
        return allocation;
    }

    private static String toJson(EnrichedAllocation allocation) throws JsonProcessingException {
        String allocationAsJson = OBJECT_MAPPER.writeValueAsString(allocation);
        LOG.info("Sending: " + allocationAsJson);
        return allocationAsJson;
    }
}
