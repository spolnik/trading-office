package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvExchangeRepositoryIntegrationTest {

    @Test
    public void returns_exchange_from_underlying_csv_file() throws Exception {
        CsvExchangeRepository repository = new CsvExchangeRepository();

        Exchange nasdaq = repository.getByMicCode("XNAS");

        assertThat(nasdaq).isEqualToComparingFieldByField(exchange());
    }

    private static Exchange exchange() {
        Exchange exchange = new Exchange();

        exchange.setAcronym("NASDAQ");
        exchange.setCity("NEW YORK");
        exchange.setCountry("UNITED STATES OF AMERICA");
        exchange.setCountryCode("US");
        exchange.setName("NASDAQ - ALL MARKETS");
        exchange.setMic("XNAS");
        exchange.setWebsite("WWW.NASDAQ.COM");

        return exchange;
    }
}