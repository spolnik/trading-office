package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentSpec {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void can_be_parsed_to_json_and_converted_back_to_object() throws Exception {

        String instrumentAsJson = objectMapper.writeValueAsString(
                instrument()
        );

        Instrument instrumentFromJson = objectMapper.readValue(
                instrumentAsJson, Instrument.class
        );

        assertThat(instrumentFromJson).isEqualToComparingFieldByField(instrument());

        System.out.println();
    }

    private Instrument instrument() {
        Instrument instrument = new Instrument();

        instrument.setCurrency("USD");
        instrument.setExchange("Nasdaq");
        instrument.setName("Amazon Stocks");
        instrument.setSymbol("AMZN");
        instrument.setPrice(BigDecimal.TEN);

        return instrument;
    }
}
