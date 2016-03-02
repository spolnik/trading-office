package com.trading;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllocationEnricherSpec {

    private static final String PARTY_NAME = "PARTY_NAME";
    private MarketDataClient marketDataClient;
    private CounterpartyClient counterpartyClient;

    private AllocationEnricher enricher;

    @Before
    public void setUp() throws Exception {
        marketDataClient = mock(MarketDataClient.class);
        counterpartyClient = mock(CounterpartyClient.class);

        when(counterpartyClient.getPartyName(any())).thenReturn(PARTY_NAME);
        when(counterpartyClient.getExchange(any())).thenReturn(TestData.exchange());

        InstrumentDetails instrumentDetails = mock(InstrumentDetails.class);

        when(instrumentDetails.getTicker()).thenReturn("AMZN");

        when(marketDataClient.getInstrumentDetails(any())).thenReturn(instrumentDetails);
        when(marketDataClient.getInstrument("AMZN")).thenReturn(TestData.instrument());

        enricher = new AllocationEnricher(marketDataClient, counterpartyClient);
    }

    @Test(expected = IOException.class)
    public void throws_exception_if_cannot_return_instrument_details() throws Exception {
        when(marketDataClient.getInstrumentDetails(any())).thenReturn(null);
        enricher.process(TestData.allocationReport());
    }

    @Test
    public void uses_instruments_api_to_get_instrument_details() throws Exception {

        enricher.process(TestData.allocationReport());
        verify(marketDataClient).getInstrumentDetails("2000019");
    }

    @Test
    public void uses_instruments_api_to_get_instrument() throws Exception {

        enricher.process(TestData.allocationReport());
        verify(marketDataClient).getInstrument("AMZN");
    }

    @Test
    public void uses_counterparty_api_to_get_exchange() throws Exception {

        enricher.process(TestData.allocationReport());
        verify(counterpartyClient).getExchange("XNAS");
    }

    @Test
    public void uses_counterparty_api_to_get_counterparty() throws Exception {

        enricher.process(TestData.allocationReport());
        verify(counterpartyClient).getPartyName("CUSTUS");
    }

    @Test
    public void uses_counterparty_api_to_get_executing_party() throws Exception {

        enricher.process(TestData.allocationReport());
        verify(counterpartyClient).getPartyName("TROF");
    }

    @Test
    public void enriches_counterparty_name() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getCounterpartyName()).isEqualTo(PARTY_NAME);
    }

    @Test
    public void enriches_executing_party_name() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getExecutingPartyName()).isEqualTo(PARTY_NAME);
    }

    @Test
    public void enriches_exchange_name() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getExchangeName()).isEqualTo(TestData.exchange().getName());
    }

    @Test
    public void enriches_exchange_acronym() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getExchangeAcronym()).isEqualTo(TestData.exchange().getAcronym());
    }

    @Test
    public void enriches_exchange_city() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getExchangeCity()).isEqualTo(TestData.exchange().getCity());
    }

    @Test
    public void enriches_exchange_country_code() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getCountryCode()).isEqualTo(TestData.exchange().getCountryCode());
    }

    @Test
    public void enriches_exchange_country() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getCountry()).isEqualTo(TestData.exchange().getCountry());
    }

    @Test
    public void enriches_instrument_symbol() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getInstrumentSymbol()).isEqualTo(TestData.instrument().getSymbol());
    }

    @Test
    public void enriches_instrument_currency() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getInstrumentCurrency()).isEqualTo(TestData.instrument().getCurrency());
    }

    @Test
    public void enriches_instrument_exchange() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getInstrumentExchange()).isEqualTo(TestData.instrument().getExchange());
    }

    @Test
    public void enriches_instrument_price() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getInstrumentPrice()).isEqualTo(TestData.instrument().getPrice());
    }

    @Test
    public void enriches_instrument_name() throws Exception {
        EnrichedAllocation enrichedAllocation = enricher.process(TestData.allocationReport());

        assertThat(enrichedAllocation.getInstrumentName()).isEqualTo(TestData.instrument().getName());
    }
}