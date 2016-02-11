package com.trading;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllocationMessageEnrichmentListenerSpec {

    private InstrumentsApi instrumentsApi;
    private CounterpartyApi counterpartyApi;

    private AllocationMessageEnrichmentListener listener;

    @Before
    public void setUp() throws Exception {
        instrumentsApi = mock(InstrumentsApi.class);
        counterpartyApi = mock(CounterpartyApi.class);
        listener = new AllocationMessageEnrichmentListener(instrumentsApi, counterpartyApi);
    }

    @Test(expected = IOException.class)
    public void throws_exception_if_cannot_return_instrument_details() throws Exception {
        when(instrumentsApi.getInstrumentDetails(any(), any())).thenReturn(null);
        listener.processAllocationReport(allocationReportAsJson());
    }

    @Test
    public void uses_instruments_api_to_get_instrument_details() throws Exception {
        setupInstrumentsApi();

        listener.processAllocationReport(allocationReportAsJson());
        verify(instrumentsApi).getInstrumentDetails("2000019", InstrumentType.SEDOL);
    }

    @Test
    public void uses_instruments_api_to_get_instrument() throws Exception {
        setupInstrumentsApi();

        listener.processAllocationReport(allocationReportAsJson());
        verify(instrumentsApi).getInstrument("AMZN");
    }

    @Test
    public void uses_counterparty_api_to_get_exchange() throws Exception {
        setupInstrumentsApi();

        listener.processAllocationReport(allocationReportAsJson());
        verify(counterpartyApi).getExchange("XNAS");
    }

    @Test
    public void uses_counterparty_api_to_get_counterparty() throws Exception {
        setupInstrumentsApi();

        listener.processAllocationReport(allocationReportAsJson());
        verify(counterpartyApi).getParty("CUSTUS");
    }

    @Test
    public void uses_counterparty_api_to_get_executing_party() throws Exception {
        setupInstrumentsApi();

        listener.processAllocationReport(allocationReportAsJson());
        verify(counterpartyApi).getParty("TROF");
    }

    private String allocationReportAsJson() throws com.fasterxml.jackson.core.JsonProcessingException {
        return objectMapper().toJson(TestData.allocationReport());
    }

    private void setupInstrumentsApi() {
        InstrumentDetails instrumentDetails = mock(InstrumentDetails.class);

        when(instrumentDetails.getTicker()).thenReturn("AMZN");

        when(instrumentsApi.getInstrumentDetails(any(), any())).thenReturn(instrumentDetails);
    }
}