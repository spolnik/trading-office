package com.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

@RestController
@RequestMapping("/api")
class FinanceDataController {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceDataController.class);

    private final Converter<Stock, Instrument> stockToInstrumentConverter;

    @Autowired
    public FinanceDataController(Converter<Stock, Instrument> stockToInstrumentConverter) {

        this.stockToInstrumentConverter = stockToInstrumentConverter;
    }

    @RequestMapping("instrument/{symbol}")
    public Instrument getInstrument(@PathVariable String symbol) {
        try {
            Stock stock = YahooFinance.get(symbol);
            return stockToInstrumentConverter.convert(stock);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        return Instrument.empty();
    }
}
