package com.trading;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {InstrumentsApiIntegrationTest.class, RestInstrumentsApi.class})
@PropertySource("classpath:app.properties")
public class InstrumentsApiIntegrationTest {

    @Autowired
    private RestInstrumentsApi instrumentsApi;

    @Test
    public void returns_instrument_details_for_given_sedol_id() throws Exception {
        InstrumentDetails instrumentDetails = instrumentsApi.getInstrumentDetails(
                "2000019", InstrumentType.SEDOL
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
