package com.trading;

import org.csveed.api.CsvClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class CounterpartyController {
    private static final Logger LOG = LoggerFactory.getLogger(CounterpartyController.class);

    private final Map<String, Party> parties = new ConcurrentHashMap<>();

    public CounterpartyController() throws FileNotFoundException {
        InputStream resourceAsStream = CounterpartyController.class
                .getClassLoader().getResourceAsStream("parties.csv");

        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);

        CsvClientImpl<Party> csvClient = new CsvClientImpl<>(reader, Party.class);
        csvClient.readBeans()
                .stream()
                .forEach(counterparty -> parties.put(counterparty.getId(), counterparty));
        LOG.info("Successfully load all parties.");
    }

    @RequestMapping("party/{id}")
    public Party getCounterparty(@PathVariable String id) {
        return parties.getOrDefault(id, new Party());
    }
}
