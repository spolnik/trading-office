package com.trading;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;

@Service
public class StockToInstrumentConverter implements Converter<Stock, Instrument> {

    @Override
    public Instrument convert(Stock stock) {

        Instrument instrument = new Instrument();

        instrument.setSymbol(stock.getSymbol());
        instrument.setName(stock.getName() + " Stocks");
        instrument.setCurrency(stock.getCurrency());

        return instrument;
    }
}
