package com.trading;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllocationMessageEnrichmentListenerSpec {

    private InstrumentsApi instrumentsApi;
    private AllocationMessageEnrichmentListener listener;

    @Before
    public void setUp() throws Exception {
        instrumentsApi = mock(InstrumentsApi.class);
        CounterpartyApi counterpartyApi = mock(CounterpartyApi.class);
        listener = new AllocationMessageEnrichmentListener(instrumentsApi, counterpartyApi);
    }

    @Test(expected = IOException.class)
    public void throws_exception_if_cannot_return_instrument_details() throws Exception {
        when(instrumentsApi.getInstrumentDetails(any(), any())).thenReturn(null);
        String json = objectMapper().toJson(TestData.allocationReport());
        listener.processAllocationReport(json);
    }
}