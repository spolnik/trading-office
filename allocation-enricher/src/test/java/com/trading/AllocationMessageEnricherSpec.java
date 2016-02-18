package com.trading;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllocationMessageEnricherSpec {

    private InstrumentsApi instrumentsApi;
    private CounterpartyApi counterpartyApi;

    private AllocationReportEnricher enricher;

    @Before
    public void setUp() throws Exception {
        instrumentsApi = mock(InstrumentsApi.class);
        counterpartyApi = mock(CounterpartyApi.class);

        PartyResponse partyResponse = new PartyResponse();
        partyResponse.setName("DUMMY");
        when(counterpartyApi.getParty(any())).thenReturn(partyResponse);

        enricher = new AllocationReportEnricher(instrumentsApi, counterpartyApi);
    }

    @Test(expected = IOException.class)
    public void throws_exception_if_cannot_return_instrument_details() throws Exception {
        when(instrumentsApi.getInstrumentDetails(any())).thenReturn(null);
        enricher.process(TestData.allocationReport());
    }

    @Test
    public void uses_instruments_api_to_get_instrument_details() throws Exception {
        setupInstrumentsApi();

        enricher.process(TestData.allocationReport());
        verify(instrumentsApi).getInstrumentDetails("2000019");
    }

    @Test
    public void uses_instruments_api_to_get_instrument() throws Exception {
        setupInstrumentsApi();

        enricher.process(TestData.allocationReport());
        verify(instrumentsApi).getInstrument("AMZN");
    }

    @Test
    public void uses_counterparty_api_to_get_exchange() throws Exception {
        setupInstrumentsApi();

        enricher.process(TestData.allocationReport());
        verify(counterpartyApi).getExchange("XNAS");
    }

    @Test
    public void uses_counterparty_api_to_get_counterparty() throws Exception {
        setupInstrumentsApi();

        enricher.process(TestData.allocationReport());
        verify(counterpartyApi).getParty("CUSTUS");
    }

    @Test
    public void uses_counterparty_api_to_get_executing_party() throws Exception {
        setupInstrumentsApi();

        enricher.process(TestData.allocationReport());
        verify(counterpartyApi).getParty("TROF");
    }

    private void setupInstrumentsApi() {
        InstrumentDetails instrumentDetails = mock(InstrumentDetails.class);

        when(instrumentDetails.getTicker()).thenReturn("AMZN");

        when(instrumentsApi.getInstrumentDetails(any())).thenReturn(instrumentDetails);
    }
}