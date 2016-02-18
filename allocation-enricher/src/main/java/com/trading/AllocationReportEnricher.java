package com.trading;

import java.io.IOException;

public class AllocationReportEnricher {

    private final InstrumentsApi instrumentsApi;
    private final CounterpartyApi counterpartyApi;

    public AllocationReportEnricher(InstrumentsApi instrumentsApi, CounterpartyApi counterpartyApi) {
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
        InstrumentDetails instrumentDetails = requestInstrumentDetails(
                allocationReport.getSecurityId()
        );

        Instrument instrument = instrumentsApi.getInstrument(instrumentDetails.getTicker());
        allocationReport.setInstrument(instrument);
    }

    private InstrumentDetails requestInstrumentDetails(
            String securityId) throws IOException {

        InstrumentDetails instrumentDetails = instrumentsApi.getInstrumentDetails(securityId);

        if (instrumentDetails == null) {
            throw new IOException("Cannot read instrument details");
        }

        return instrumentDetails;
    }

    private void enrichWithExchange(AllocationReport allocationReport) {
        Exchange exchange = counterpartyApi.getExchange(
                allocationReport.getExchange().getMic()
        );

        allocationReport.setExchange(exchange);
    }

    private void enrichWithCounterparty(AllocationReport allocationReport) {
        PartyResponse counterparty = counterpartyApi.getParty(
                allocationReport.getCounterpartyId()
        );

        allocationReport.setCounterpartyName(counterparty.getName());
    }

    private void enrichWithExecutingParty(AllocationReport allocationReport) {
        PartyResponse executingParty = counterpartyApi.getParty(
                allocationReport.getExecutingPartyId()
        );

        allocationReport.setExecutingPartyName(executingParty.getName());
    }
}
