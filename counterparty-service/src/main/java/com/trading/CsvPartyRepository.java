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

class CsvPartyRepository implements PartyRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CsvPartyRepository.class);

    private final Map<String, String> parties = new ConcurrentHashMap<>();

    public CsvPartyRepository() {

        InputStream resourceAsStream = CounterpartyController.class
                .getClassLoader().getResourceAsStream("parties.csv");

        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);

        CsvClientImpl<CsvParty> csvClient = new CsvClientImpl<>(reader, CsvParty.class);
        csvClient.readBeans()
                .stream()
                .forEach(counterparty -> parties.put(counterparty.getId(), counterparty.getName()));
        LOG.info("Successfully load all parties.");
    }

    @Override
    public String getName(String id) {
        return parties.getOrDefault(id, "");
    }
}
