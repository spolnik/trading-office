package com.trading;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FinanceDataControllerSpec {

    private static final String INTEL_SYMBOL = "INTC";

    private static Instrument instrument;

    private static final FinanceDataController FINANCE_DATA_CONTROLLER = new FinanceDataController(
            new StockToInstrumentConverter()
    );

    @BeforeClass
    public static void setUp() throws Exception {
        instrument = FINANCE_DATA_CONTROLLER.getInstrument(INTEL_SYMBOL);
    }

    @Test
    public void maps_symbol_of_instrument() throws Exception {
        assertThat(instrument.getSymbol()).isEqualTo(INTEL_SYMBOL);
    }

    @Test
    public void maps_name_of_instrument() throws Exception {
        assertThat(instrument.getName()).isEqualTo("Intel Corporation Stocks");
    }

    @Test
    public void maps_currency_of_instrument() throws Exception {
        assertThat(instrument.getCurrency()).isEqualTo("USD");
    }

    @Test
    public void maps_exchange_of_instrument() throws Exception {
        assertThat(instrument.getExchange()).isEqualTo("NMS");
    }

    @Test
    public void maps_bid_price_of_instrument() throws Exception {
        assertThat(instrument.getPrice()).isBetween(new BigDecimal(20), new BigDecimal(40));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void returns_empty_instrument_if_exception_is_raised() throws Exception {
        Converter converter = mock(Converter.class);
        when(converter.convert(any())).thenThrow(IOException.class);

        FinanceDataController controller = new FinanceDataController(
                converter
        );

        assertThat(controller.getInstrument("DUMMY")).isEqualToComparingFieldByField(
                Instrument.empty()
        );
    }
}
