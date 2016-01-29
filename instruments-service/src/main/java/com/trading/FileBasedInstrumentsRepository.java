package com.trading;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileBasedInstrumentsRepository implements InstrumentsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(
            FileBasedInstrumentsRepository.class
    );

    private final List<InstrumentDetails> instruments = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileBasedInstrumentsRepository() {
        try (InputStream inputStream = instrumentsDataFileStream()) {
            loadInstruments(inputStream);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private InputStream instrumentsDataFileStream() {

        return FileBasedInstrumentsRepository.class.getClassLoader().getResourceAsStream(
                "instruments.json"
        );
    }

    private void loadInstruments(InputStream inputStream) throws IOException {

        readInstruments(inputStream).forEach(
                this.instruments::add
        );
    }

    private List<InstrumentDetails> readInstruments(InputStream inputStream) throws IOException {
        return objectMapper.readValue(
                inputStream, new TypeReference<List<InstrumentDetails>>() {
                });
    }

    @Override
    public InstrumentDetails queryBySedol(String sedol) {

        return instruments.stream()
                .filter(x -> sedol.equalsIgnoreCase(x.getSedol()))
                .findFirst()
                .orElse(InstrumentDetails.empty());
    }
}
