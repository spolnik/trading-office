package com.trading;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentsServiceIntegrationTest {

    @Before
    public void setUp() throws Exception {
        InstrumentsApplication.main(new String[0]);
    }

    @Test
    public void service_returns_instrument_details_for_given_sedol_id() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        InstrumentDetails instrumentDetails = restTemplate.getForObject(
                "http://localhost:9005/api/instruments/sedol/2000019",
                InstrumentDetails.class
        );

        assertThat(instrumentDetails.getTicker()).isEqualTo("AMZN");
        assertThat(instrumentDetails.getSedol()).isEqualTo("2000019");
        assertThat(instrumentDetails.getCusip()).isEqualTo("023135106");
        assertThat(instrumentDetails.getName()).isEqualTo("AMAZON.COM INC");
        assertThat(instrumentDetails.getRic()).isEqualTo("AMZN.OQ");
    }
}
