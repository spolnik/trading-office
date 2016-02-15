package com.trading;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InstrumentsControllerSpec {

    private static final String DUMMY = "DUMMY";

    private InstrumentsRepository instrumentRepository = mock(InstrumentsRepository.class);

    private InstrumentsController controller;

    @Before
    public void setUp() throws Exception {

        controller = new InstrumentsController(instrumentRepository);
    }

    @Test
    public void uses_instruments_repository_to_query_for_instrument() throws Exception {

        controller.getInstrumentDetailsBySedol(DUMMY);

        verify(instrumentRepository).queryBySedol(DUMMY);
    }

    @Test
    public void returns_instrument_based_on_sedol() throws Exception {

        InstrumentDetails instrumentDetails = mock(InstrumentDetails.class);
        when(instrumentRepository.queryBySedol(DUMMY)).thenReturn(instrumentDetails);

        InstrumentDetails result = controller.getInstrumentDetailsBySedol(DUMMY);

        assertThat(result).isEqualTo(instrumentDetails);
    }
}