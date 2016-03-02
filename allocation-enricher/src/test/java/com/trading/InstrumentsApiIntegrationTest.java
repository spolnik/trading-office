package com.trading;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentsApiIntegrationTest {

    private InstrumentsApiClient instrumentsApi = new InstrumentsApiClient(
            new RestTemplate(),
            "market-data-service.herokuapp.com"
    );

    @Test
    public void returns_instrument_details_for_given_sedol_id() throws Exception {
        InstrumentDetails instrumentDetails = instrumentsApi.getInstrumentDetails(
                "2000019"
        );

        assertThat(instrumentDetails).isEqualToComparingFieldByField(instrumentDetails());
    }

    @Test
    public void returns_amazon_instrument_when_queried_by_amazon_symbol() throws Exception {
        Instrument instrument = instrumentsApi.getInstrument("AMZN");

        assertThat(instrument).isEqualToIgnoringGivenFields(TestData.instrument(), "price");
    }

    private InstrumentDetails instrumentDetails() {
        InstrumentDetails instrumentDetails = new InstrumentDetails();

        instrumentDetails.setTicker("AMZN");
        instrumentDetails.setName("AMAZON.COM INC");
        instrumentDetails.setSecurityType("Common Stock");

        return instrumentDetails;
    }
}
