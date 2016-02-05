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
public class CounterpartyController {
    private static final Logger LOG = LoggerFactory.getLogger(CounterpartyController.class);

    private final Map<String, Counterparty> counterparties = new ConcurrentHashMap<>();

    public CounterpartyController() throws FileNotFoundException {
        URL path = Resources.getResource("counterparties.csv");
        FileReader reader = new FileReader(path.getFile());

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
