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
                allocationReport.getMicCode()
        );

        allocationReport.setCountry(exchange.getCountry());
        allocationReport.setCountryCode(exchange.getCountryCode());
        allocationReport.setExchangeAcronym(exchange.getAcronym());
        allocationReport.setExchangeCity(exchange.getCity());
        allocationReport.setExchangeName(exchange.getName());
    }

    private void enrichWithCounterparty(AllocationReport allocationReport) {
        String counterparty = counterpartyApi.getPartyName(
                allocationReport.getCounterpartyId()
        );

        allocationReport.setCounterpartyName(counterparty);
    }

    private void enrichWithExecutingParty(AllocationReport allocationReport) {
        String executingParty = counterpartyApi.getPartyName(
                allocationReport.getExecutingPartyId()
        );

        allocationReport.setExecutingPartyName(executingParty);
    }
}
