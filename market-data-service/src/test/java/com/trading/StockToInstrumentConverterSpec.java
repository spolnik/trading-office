package com.trading;

import org.junit.Before;
import org.junit.Test;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockToInstrumentConverterSpec {

    private static final BigDecimal PRICE = BigDecimal.valueOf(2.34);

    private static final String EXCHANGE = "NMS";
    private static final String SYMBOL = "INTC";
    private static final String CURRENCY = "USD";

    private Instrument instrument;

    @Before
    public void setUp() throws Exception {
        StockToInstrumentConverter converter = new StockToInstrumentConverter();
        instrument = converter.convert(intelStock());
    }

    @Test
    public void maps_symbol_of_instrument() throws Exception {
        assertThat(instrument.getSymbol()).isEqualTo(SYMBOL);
    }

    @Test
    public void maps_name_of_instrument() throws Exception {
        assertThat(instrument.getName()).isEqualTo("Intel Corporation Stocks");
    }

    @Test
    public void maps_currency_of_instrument() throws Exception {
        assertThat(instrument.getCurrency()).isEqualTo(CURRENCY);
    }

    @Test
    public void maps_exchange_of_instrument() throws Exception {
        assertThat(instrument.getExchange()).isEqualTo(EXCHANGE);
    }

    @Test
    public void maps_bid_price_of_instrument() throws Exception {
        assertThat(instrument.getPrice()).isEqualTo(PRICE);
    }

    private Stock intelStock() {
        Stock intelStock = mock(Stock.class);

        when(intelStock.getCurrency()).thenReturn(CURRENCY);
        when(intelStock.getSymbol()).thenReturn(SYMBOL);
        when(intelStock.getStockExchange()).thenReturn(EXCHANGE);
        when(intelStock.getName()).thenReturn("Intel Corporation");

        StockQuote quote = mock(StockQuote.class);
        when(quote.getBid()).thenReturn(PRICE);
        when(intelStock.getQuote()).thenReturn(quote);

        return intelStock;
    }
}