package com.trading;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class FinancialDataServiceIntegrationTest {

    @BeforeClass
    public static void setUp() throws Exception {
        FinancialDataApplication.main(new String[0]);
    }

    @Test
    public void service_returns_data_for_intel_symbol() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Instrument instrument = restTemplate.getForObject(
                "http://localhost:9004/api/instrument/INTC",
                Instrument.class
        );

        assertThat(instrument.getName()).isEqualTo("Intel Corporation Stocks");
        assertThat(instrument.getExchange()).isEqualTo("NMS");
        assertThat(instrument.getCurrency()).isEqualTo("USD");
        assertThat(instrument.getSymbol()).isEqualTo("INTC");
    }

    @Test
    public void service_returns_instrument_details_for_given_sedol_id() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        OpenFigiResponse instrumentDetails = restTemplate.getForObject(
                "http://localhost:9004/api/instruments/sedol/2000019",
                OpenFigiResponse.class
        );

        assertThat(instrumentDetails).isEqualToComparingFieldByField(
                TestData.openFigiResponse()
        );
    }
}
