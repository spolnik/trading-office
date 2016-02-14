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

    private final Map<String, Party> parties = new ConcurrentHashMap<>();

    public CsvPartyRepository() {

        InputStream resourceAsStream = CounterpartyController.class
                .getClassLoader().getResourceAsStream("parties.csv");

        Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);

        CsvClientImpl<Party> csvClient = new CsvClientImpl<>(reader, Party.class);
        csvClient.readBeans()
                .stream()
                .forEach(counterparty -> parties.put(counterparty.getId(), counterparty));
        LOG.info("Successfully load all parties.");
    }

    @Override
    public Party getById(String id) {
        return parties.getOrDefault(id, new Party());
    }
}
