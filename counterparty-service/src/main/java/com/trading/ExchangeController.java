package com.trading;

import com.google.common.io.Resources;
import org.csveed.api.CsvClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class ExchangeController {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeController.class);

    private final Map<String, Exchange> exchanges = new ConcurrentHashMap<>();

    public ExchangeController() throws FileNotFoundException {
        URL micCodesPath = Resources.getResource("mic_codes.csv");
        FileReader reader = new FileReader(micCodesPath.getFile());

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
