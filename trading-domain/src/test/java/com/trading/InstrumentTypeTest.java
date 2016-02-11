package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentTypeTest {

    @Test(expected = UnsupportedOperationException.class)
    public void throws_exception_for_unsupported_type() throws Exception {
        InstrumentType.getInstrumentType("1");
    }

    @Test
    public void returns_sedol_for_id_2() throws Exception {
        assertThat(InstrumentType.getInstrumentType("2"))
                .isEqualTo(InstrumentType.SEDOL);
    }
}