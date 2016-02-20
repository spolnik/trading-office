package com.trading;

import org.csveed.api.CsvClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class CsvExchangeRepository implements ExchangeRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CsvExchangeRepository.class);

    private final Map<String, CsvExchange> exchanges = new ConcurrentHashMap<>();

    public CsvExchangeRepository() {
        InputStream resourceAsStream = CounterpartyController.class
                .getClassLoader().getResourceAsStream("mic_codes.csv");

        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);

        CsvClientImpl<CsvExchange> csvClient = new CsvClientImpl<>(reader, CsvExchange.class);
        csvClient.readBeans()
                .stream()
                .forEach(exchange -> exchanges.put(exchange.getMic(), exchange));
        LOG.info("Successfully load all exchanges.");
    }

    @Override
    public CsvExchange getByMicCode(String micCode) {
        return exchanges.getOrDefault(micCode, new CsvExchange());
    }
}
