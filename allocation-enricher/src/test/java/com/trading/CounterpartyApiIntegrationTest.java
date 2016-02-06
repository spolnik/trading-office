package com.trading;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {CounterpartyApiIntegrationTest.class, RestCounterpartyApi.class})
@PropertySource("classpath:app.properties")
public class CounterpartyApiIntegrationTest {

    @Autowired
    private RestCounterpartyApi counterpartyApi;

    @Test
    public void returns_exchange_for_given_mic_code() throws Exception {
        Exchange nasdaq = counterpartyApi.getExchange(
                "XNAS"
        );

        assertThat(nasdaq).isEqualToComparingFieldByField(TestData.exchange());
    }

    @Test
    public void returns_trading_office_ltd_when_queried_by_TROF_party_id() throws Exception {
        Party executingParty = counterpartyApi.getParty("TROF");

        assertThat(executingParty).isEqualToComparingFieldByField(
                TestData.executingParty()
        );
    }
}
