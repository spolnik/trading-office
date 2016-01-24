package com.trading;

public interface InstrumentsApi {
    InstrumentDetails getInstrumentDetails(String securityId, SecurityIDSource securityIdSource);
    Instrument getInstrument(String ticker);
}
