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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class CounterpartyController {
    private static final Logger LOG = LoggerFactory.getLogger(CounterpartyController.class);

    private final Map<String, Counterparty> counterparties = new ConcurrentHashMap<>();

    public CounterpartyController() throws FileNotFoundException {
        InputStream resourceAsStream = CounterpartyController.class
                .getClassLoader().getResourceAsStream("counterparties.csv");

        Reader reader = new InputStreamReader(resourceAsStream);

        CsvClientImpl<Counterparty> csvClient = new CsvClientImpl<>(reader, Counterparty.class);
        csvClient.readBeans()
                .stream()
                .forEach(counterparty -> counterparties.put(counterparty.getId(), counterparty));
        LOG.info("Successfully load all counterparties.");
    }

    @RequestMapping("counterparty/{id}")
    public Counterparty getCounterparty(@PathVariable String id) {
        return counterparties.getOrDefault(id, new Counterparty());
    }
}
