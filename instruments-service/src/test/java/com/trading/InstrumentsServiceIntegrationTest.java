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
        OpenFigiResponse instrumentDetails = restTemplate.getForObject(
                "http://localhost:9005/api/instruments/sedol/2000019",
                OpenFigiResponse.class
        );

        assertThat(instrumentDetails).isEqualToComparingFieldByField(
                TestData.openFigiResponse()
        );
    }
}
