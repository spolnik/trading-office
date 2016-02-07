package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.trading.DomainObjectMapper.objectMapper;

@Component
class AllocationMessageEnrichmentListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllocationMessageEnrichmentListener.class);

    private final InstrumentsApi instrumentsApi;
    private final CounterpartyApi counterpartyApi;

    @Autowired
    public AllocationMessageEnrichmentListener(
            InstrumentsApi instrumentsApi, CounterpartyApi counterpartyApi) {

        this.instrumentsApi = instrumentsApi;
        this.counterpartyApi = counterpartyApi;
    }

    @JmsListener(destination = Queues.RECEIVED_JSON_ALLOCATION_REPORT_QUEUE, containerFactory = "jmsContainerFactory")
    @SendTo(Queues.ENRICHED_JSON_ALLOCATION_REPORT_EMAIL_QUEUE)
    public String processAllocationReport(String message) throws IOException {

        return toJson(enrich(fromJson(message)));
    }

    private static String toJson(AllocationReport allocationReport) throws JsonProcessingException {
        String allocationReportAsJson = objectMapper().toJson(allocationReport);
        LOG.info("Sending: " + allocationReportAsJson);
        return allocationReportAsJson;
    }

    private static AllocationReport fromJson(String message) throws IOException {
        AllocationReport allocationReport = objectMapper().toAllocationReport(message);
        LOG.info("Received: " + allocationReport);
        return allocationReport;
    }

    private AllocationReport enrich(AllocationReport allocationReport) throws IOException {

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
