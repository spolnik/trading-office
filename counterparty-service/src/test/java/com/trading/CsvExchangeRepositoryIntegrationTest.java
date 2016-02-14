package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvExchangeRepositoryIntegrationTest {

    @Test
    public void returns_exchange_from_underlying_csv_file() throws Exception {
        CsvExchangeRepository repository = new CsvExchangeRepository();

        Exchange nasdaq = repository.getByMicCode("XNAS");

        assertThat(nasdaq).isEqualToComparingFieldByField(TestData.exchange());
    }
}