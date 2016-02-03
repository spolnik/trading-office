package com.trading;

public interface InstrumentsApi {
    InstrumentDetails getInstrumentDetails(String securityId, InstrumentType instrumentType);
    Instrument getInstrument(String ticker);
}
