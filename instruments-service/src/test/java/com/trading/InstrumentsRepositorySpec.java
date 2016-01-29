package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentsRepositorySpec {

    private InstrumentsRepository repository = new FileBasedInstrumentsRepository();

    @Test
    public void can_query_by_sedol() throws Exception {

        InstrumentDetails instrumentDetails = repository.queryBySedol("2000019");

        assertThat(instrumentDetails.getTicker()).isEqualTo(
                "AMZN"
        );
    }

    @Test
    public void returns_empty_instrument_details_if_invalid_id() throws Exception {

        InstrumentDetails instrumentDetails = repository.queryBySedol("DUMMY");

        assertThat(instrumentDetails).isEqualToComparingFieldByField(
                InstrumentDetails.empty()
        );
    }

    @Test
    public void returns_instrument_data_based_on_sedol_id_through_open_figi_api() throws Exception {
        OpenFigiInstrumentsRepository repository = new OpenFigiInstrumentsRepository();

        InstrumentDetails instrumentDetails = repository.queryBySedol("2000019");

        assertThat(instrumentDetails).isEqualToComparingFieldByField(
                TestData.instrumentDetails()
        );
    }
}
