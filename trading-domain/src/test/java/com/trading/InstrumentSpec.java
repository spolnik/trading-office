package com.trading;

import org.junit.Test;

import java.math.BigDecimal;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentSpec {

    @Test
    public void can_be_parsed_to_json_and_converted_back_to_object() throws Exception {

        String instrumentAsJson = objectMapper().toJson(instrument());

        Instrument instrumentFromJson = objectMapper().toInstrument(instrumentAsJson);

        assertThat(instrumentFromJson).isEqualToComparingFieldByField(instrument());

        System.out.println(instrumentFromJson);
    }

    @Test
    public void two_different_instruments_with_same_symbol_exchange_and_currency_are_equal() throws Exception {
        Instrument expectedInstrument = instrument();

        Instrument instrument = new Instrument();
        instrument.setSymbol(expectedInstrument.getSymbol());
        instrument.setExchange(expectedInstrument.getExchange());
        instrument.setCurrency(expectedInstrument.getCurrency());

        assertThat(instrument).isEqualTo(expectedInstrument);
    }

    @Test
    public void two_different_instruments_are_not_equal_if_exchange_is_different() throws Exception {
        Instrument instrumentWithDifferentExchange = instrument();
        instrumentWithDifferentExchange.setExchange("NYSE");

        assertThat(instrumentWithDifferentExchange).isNotEqualTo(instrument());
    }

    @Test
    public void two_different_instruments_are_not_equal_if_symbol_is_different() throws Exception {
        Instrument instrumentWithDifferentSymbol = instrument();
        instrumentWithDifferentSymbol.setSymbol("GOOG");

        assertThat(instrumentWithDifferentSymbol).isNotEqualTo(instrument());
    }

    @Test
    public void two_different_instruments_are_not_equal_if_currency_is_different() throws Exception {
        Instrument instrumentWithDifferentCurrency = instrument();
        instrumentWithDifferentCurrency.setCurrency("EUR");

        assertThat(instrumentWithDifferentCurrency).isNotEqualTo(instrument());
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
