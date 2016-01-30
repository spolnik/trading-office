package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

class DomainObjectMapper {

    private static final DomainObjectMapper DOMAIN_OBJECT_MAPPER_INSTANCE = new DomainObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private DomainObjectMapper() {
        // empty
    }

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public AllocationReport toAllocationReport(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, AllocationReport.class);
    }

    public Confirmation toConfirmation(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, Confirmation.class);
    }

    public InstrumentDetails toInstrumentDetails(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, InstrumentDetails.class);
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
