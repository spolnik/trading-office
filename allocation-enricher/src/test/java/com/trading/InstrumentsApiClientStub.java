package com.trading;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class InstrumentsApiClientStub implements InstrumentsApi {

    @Override
    public InstrumentDetails getInstrumentDetails(String securityId) {

        InstrumentDetails instrumentDetails = new InstrumentDetails();
        instrumentDetails.setTicker("AMZN");

        return instrumentDetails;
    }

    @Override
    public Instrument getInstrument(String ticker) {
        return TestData.instrument();
    }
}
