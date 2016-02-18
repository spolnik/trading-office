package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CounterpartyApiIntegrationTest {

    private CounterpartyApiClient counterpartyApi = new CounterpartyApiClient("http://counterparty-service.herokuapp.com");

    @Test
    public void returns_exchange_for_given_mic_code() throws Exception {
        Exchange nasdaq = counterpartyApi.getExchange(
                "XNAS"
        );

        assertThat(nasdaq).isEqualToComparingFieldByField(TestData.exchange());
    }

    @Test
    public void returns_trading_office_ltd_when_queried_by_TROF_party_id() throws Exception {
        PartyResponse executingParty = counterpartyApi.getParty("TROF");

        assertThat(executingParty).isEqualToComparingFieldByField(
                TestData.executingParty()
        );
    }
}
