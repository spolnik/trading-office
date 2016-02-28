package com.trading;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YahooApiIntegrationTest {

    private static final String INTEL_SYMBOL = "INTC";

    @Test
    public void returns_instrument_for_correct_symbol() throws Exception {
        YahooApi yahooApi = new YahooApiClient(new StockToInstrumentConverter());

        Instrument instrument = yahooApi.getInstrument(INTEL_SYMBOL);

        assertThat(instrument).isEqualToIgnoringGivenFields(
                instrument(), "price"
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void returns_empty_instrument_if_exception_is_raised() throws Exception {
        Converter converter = mock(Converter.class);
        when(converter.convert(any())).thenThrow(IOException.class);

        YahooApi yahooApi = new YahooApiClient(
                converter
        );

        assertThat(yahooApi.getInstrument("DUMMY")).isEqualToComparingFieldByField(
                Instrument.empty()
        );
    }

    private Instrument instrument() {

        Instrument instrument = new Instrument();

        instrument.setCurrency("USD");
        instrument.setExchange("NMS");
        instrument.setName("Intel Corporation Stocks");
        instrument.setSymbol("INTC");

        return instrument;
    }
}
