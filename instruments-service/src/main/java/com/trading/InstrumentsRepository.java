package com.trading;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InstrumentsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(
            InstrumentsRepository.class
    );

    private final List<InstrumentDetails> instruments = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InstrumentsRepository() {
        URL url = instrumentsDataFileUrl();
        loadInstruments(url);
    }

    private URL instrumentsDataFileUrl() {

        return InstrumentsRepository.class.getClassLoader().getResource(
                "instruments.json"
        );
    }

    private void loadInstruments(URL instrumentsJson) {
        try {
            File file = new File(instrumentsJson.getPath());
            readInstruments(file).forEach(this.instruments::add);
        } catch (Exception e) {
            LOG.error("Error reading/parsing instrument file.", e);
        }
    }

    private List<InstrumentDetails> readInstruments(File file) throws IOException {
        return objectMapper.readValue(
                file, new TypeReference<List<InstrumentDetails>>() {
                });
    }

    public InstrumentDetails queryBySedol(String sedol) {

        return instruments.stream()
                .filter(x -> sedol.toLowerCase().equals(x.getSedol().toLowerCase()))
                .findFirst()
                .orElse(InstrumentDetails.empty());
    }
}
