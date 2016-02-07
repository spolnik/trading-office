package com.trading;

interface InstrumentsApi {
    InstrumentDetails getInstrumentDetails(String securityId, InstrumentType instrumentType);
    Instrument getInstrument(String ticker);
}
