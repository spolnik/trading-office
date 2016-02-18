package com.trading;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class CounterpartyServiceIntegrationTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void setUp() throws Exception {
        CounterpartyServiceApplication.main(new String[0]);
    }

    @Test
    public void service_returns_counterparty() throws Exception {

        CsvParty party = restTemplate.getForObject(
                "http://localhost:9008/api/party/CUSTAU",
                CsvParty.class
        );

        assertThat(party.getName()).isEqualTo(
                "Customer Australia Pty Ltd."
        );
    }

    @Test
    public void service_returns_exchange() throws Exception {
        Exchange exchange = restTemplate.getForObject(
                "http://localhost:9008/api/exchange/mic/XNAS",
                Exchange.class
        );

        assertThat(exchange.getName()).isEqualTo(
            "NASDAQ - ALL MARKETS"
        );
    }
}
