package com.trading;

interface InstrumentsApi {
    InstrumentDetails getInstrumentDetails(String securityId);
    Instrument getInstrument(String ticker);
}
