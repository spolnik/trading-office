package com.trading;

import java.io.IOException;

public class AllocationEnricher {

    private final InstrumentsApi instrumentsApi;
    private final CounterpartyApi counterpartyApi;

    public AllocationEnricher(InstrumentsApi instrumentsApi, CounterpartyApi counterpartyApi) {
        this.instrumentsApi = instrumentsApi;
        this.counterpartyApi = counterpartyApi;
    }

    public EnrichedAllocation process(EnrichedAllocation allocationReport) throws IOException {

        enrichWithInstrument(allocationReport);
        enrichWithExchange(allocationReport);
        enrichWithCounterparty(allocationReport);
        enrichWithExecutingParty(allocationReport);

        return allocationReport;
    }

    private void enrichWithInstrument(EnrichedAllocation allocationReport) throws IOException {
        InstrumentDetails instrumentDetails = requestInstrumentDetails(
                allocationReport.getSecurityId()
        );

        Instrument instrument = instrumentsApi.getInstrument(instrumentDetails.getTicker());
        allocationReport.setInstrumentName(instrument.getName());
        allocationReport.setInstrumentCurrency(instrument.getCurrency());
        allocationReport.setInstrumentExchange(instrument.getExchange());
        allocationReport.setInstrumentSymbol(instrument.getSymbol());
        allocationReport.setInstrumentPrice(instrument.getPrice());
    }

    private InstrumentDetails requestInstrumentDetails(
            String securityId) throws IOException {

        InstrumentDetails instrumentDetails = instrumentsApi.getInstrumentDetails(securityId);

        if (instrumentDetails == null) {
            throw new IOException("Cannot read instrument details");
        }

        return instrumentDetails;
    }

    private void enrichWithExchange(EnrichedAllocation allocationReport) {
        Exchange exchange = counterpartyApi.getExchange(
                allocationReport.getMicCode()
        );

        allocationReport.setCountry(exchange.getCountry());
        allocationReport.setCountryCode(exchange.getCountryCode());
        allocationReport.setExchangeAcronym(exchange.getAcronym());
        allocationReport.setExchangeCity(exchange.getCity());
        allocationReport.setExchangeName(exchange.getName());
    }

    private void enrichWithCounterparty(EnrichedAllocation allocationReport) {
        String counterparty = counterpartyApi.getPartyName(
                allocationReport.getCounterpartyId()
        );

        allocationReport.setCounterpartyName(counterparty);
    }

    private void enrichWithExecutingParty(EnrichedAllocation allocationReport) {
        String executingParty = counterpartyApi.getPartyName(
                allocationReport.getExecutingPartyId()
        );

        allocationReport.setExecutingPartyName(executingParty);
    }
}
