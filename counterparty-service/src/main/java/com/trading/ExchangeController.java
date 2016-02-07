package com.trading;

import org.csveed.api.CsvClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
class ExchangeController {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeController.class);

    private final Map<String, Exchange> exchanges = new ConcurrentHashMap<>();

    public ExchangeController() {
        InputStream resourceAsStream = CounterpartyController.class
                .getClassLoader().getResourceAsStream("mic_codes.csv");

        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);

        CsvClientImpl<Exchange> csvClient = new CsvClientImpl<>(reader, Exchange.class);
        csvClient.readBeans()
                .stream()
                .forEach(exchange -> exchanges.put(exchange.getMic(), exchange));
        LOG.info("Successfully load all exchanges.");
    }

    @RequestMapping("exchange/mic/{micCode}")
    public Exchange getExchange(@PathVariable String micCode) {
        return exchanges.getOrDefault(micCode, new Exchange());
    }
}
