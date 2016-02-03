package com.trading;

import org.junit.Test;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentDetailsSpec {

    @Test
    public void can_be_parsed_to_json_and_converted_back_to_object() throws Exception {

        String instrumentDetailsAsJson = objectMapper().toJson(
                instrumentDetails()
        );

        InstrumentDetails instrumentDetailsFromJson = objectMapper().toInstrumentDetails(
                instrumentDetailsAsJson
        );

        assertThat(instrumentDetailsFromJson).isEqualToComparingFieldByField(instrumentDetails());

        System.out.println(instrumentDetailsFromJson);
    }

    private InstrumentDetails instrumentDetails() {

        InstrumentDetails instrumentDetails = new InstrumentDetails();

        instrumentDetails.setName("DUMMY_NAME");
        instrumentDetails.setTicker("DUMMY_SYMBOL");
        instrumentDetails.setSecurityType("DUMMY_TYPE");

        return instrumentDetails;
    }
}
