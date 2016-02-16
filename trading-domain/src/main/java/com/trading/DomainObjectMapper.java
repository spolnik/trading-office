package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

class DomainObjectMapper {

    private static final DomainObjectMapper DOMAIN_OBJECT_MAPPER_INSTANCE = new DomainObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private DomainObjectMapper() {
        // empty
    }

    public AllocationReport toAllocationReport(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, AllocationReport.class);
    }

    public Confirmation toConfirmation(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, Confirmation.class);
    }

    public Instrument toInstrument(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, Instrument.class);
    }

    public <T> String toJson(T value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }

    public static DomainObjectMapper objectMapper() {
        return DOMAIN_OBJECT_MAPPER_INSTANCE;
    }
}
