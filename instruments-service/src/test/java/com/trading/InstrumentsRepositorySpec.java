package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentsRepositorySpec {

    @Test
    public void can_query_by_sedol() throws Exception {
        InstrumentsRepository repository = new InstrumentsRepository();

        InstrumentDetails instrumentDetails = repository.queryBySedol("2000019");

        assertThat(instrumentDetails.getTicker()).isEqualTo("AMZN");
    }
}
