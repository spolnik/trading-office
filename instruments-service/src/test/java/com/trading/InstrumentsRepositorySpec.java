package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentsRepositorySpec {

    @Test
    public void returns_instrument_data_based_on_sedol_id_through_open_figi_api() throws Exception {
        OpenFigiInstrumentsRepository repository = new OpenFigiInstrumentsRepository();

        OpenFigiResponse instrumentDetails = repository.queryBySedol("2000019");

        assertThat(instrumentDetails).isEqualToComparingFieldByField(
                TestData.openFigiResponse()
        );
    }
}
