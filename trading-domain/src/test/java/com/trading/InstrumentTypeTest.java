package com.trading;

import org.junit.Test;

public class InstrumentTypeTest {

    @Test(expected = UnsupportedOperationException.class)
    public void throws_exception_for_unsupported_type() throws Exception {
        InstrumentType.getInstrumentType("1");
    }
}