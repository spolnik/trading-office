package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

class YahooApiClient implements YahooApi {

    private static final Logger LOG = LoggerFactory.getLogger(MarketDataController.class);

    private final Converter<Stock, Instrument> stockToInstrumentConverter;

    public YahooApiClient(Converter<Stock, Instrument> stockToInstrumentConverter) {
        this.stockToInstrumentConverter = stockToInstrumentConverter;
    }

    @Override
    public Instrument getInstrument(String symbol) {
        try {
            Stock stock = getStock(symbol);
            return stockToInstrumentConverter.convert(stock);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        return Instrument.empty();
    }

    private static Stock getStock(String symbol) throws IOException {
        return YahooFinance.get(symbol);
    }
}
