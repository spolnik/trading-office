package com.trading;

import java.util.Arrays;
import java.util.Optional;

public enum InstrumentType {
    SEDOL("2");

    private final String fixInstrumentType;

    InstrumentType(String fixInstrumentType) {
        this.fixInstrumentType = fixInstrumentType;
    }

    public static InstrumentType getInstrumentType(String fixInstrumentType) {
        Optional<InstrumentType> instrumentType = Arrays.asList(values())
                .stream()
                .filter(value -> value.fixInstrumentType.equals(fixInstrumentType))
                .findFirst();

        return instrumentType.orElseThrow(
                () -> new UnsupportedOperationException("Instrument Type is unsupported: " + fixInstrumentType)
        );
    }
}
