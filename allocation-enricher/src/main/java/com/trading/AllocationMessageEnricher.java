package com.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AllocationMessageEnricher {

    private final InstrumentsApi instrumentsApi;
    private final CounterpartyApi counterpartyApi;

    @Autowired
    public AllocationMessageEnricher(InstrumentsApi instrumentsApi, CounterpartyApi counterpartyApi) {
        this.instrumentsApi = instrumentsApi;
        this.counterpartyApi = counterpartyApi;
    }

    public AllocationReport process(AllocationReport allocationReport) throws IOException {

        enrichWithInstrument(allocationReport);
        enrichWithExchange(allocationReport);
        enrichWithCounterparty(allocationReport);
        enrichWithExecutingParty(allocationReport);

        return allocationReport;
    }

    private void enrichWithInstrument(AllocationReport allocationReport) throws IOException {
        InstrumentDetails instrumentDetails = instrumentsApi.getInstrumentDetails(
                allocationReport.getSecurityId(), allocationReport.getInstrumentType()
        );

        if (instrumentDetails == null) {
            throw new IOException("Cannot read instrument details");
        }

        Instrument instrument = instrumentsApi.getInstrument(instrumentDetails.getTicker());
        allocationReport.setInstrument(instrument);
    }

    private void enrichWithExchange(AllocationReport allocationReport) {
        Exchange exchange = counterpartyApi.getExchange(allocationReport.getExchange().getMic());
        allocationReport.setExchange(exchange);
    }

    private void enrichWithCounterparty(AllocationReport allocationReport) {
        Party counterparty = counterpartyApi.getParty(allocationReport.getCounterparty().getId());
        allocationReport.setCounterparty(counterparty);
    }

    private void enrichWithExecutingParty(AllocationReport allocationReport) {
        Party executingParty = counterpartyApi.getParty(allocationReport.getExecutingParty().getId());
        allocationReport.setExecutingParty(executingParty);
    }
}
