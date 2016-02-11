package com.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AllocationReportEnricher {

    private final InstrumentsApi instrumentsApi;
    private final CounterpartyApi counterpartyApi;

    @Autowired
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
                allocationReport.getSecurityId(), allocationReport.getInstrumentType()
        );

        Instrument instrument = instrumentsApi.getInstrument(instrumentDetails.getTicker());
        allocationReport.setInstrument(instrument);
    }

    private InstrumentDetails requestInstrumentDetails(
            String securityId, InstrumentType instrumentType) throws IOException {

        InstrumentDetails instrumentDetails = instrumentsApi.getInstrumentDetails(
                securityId, instrumentType
        );

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
        Party counterparty = counterpartyApi.getParty(
                allocationReport.getCounterparty().getId()
        );

        allocationReport.setCounterparty(counterparty);
    }

    private void enrichWithExecutingParty(AllocationReport allocationReport) {
        Party executingParty = counterpartyApi.getParty(
                allocationReport.getExecutingParty().getId()
        );

        allocationReport.setExecutingParty(executingParty);
    }
}
