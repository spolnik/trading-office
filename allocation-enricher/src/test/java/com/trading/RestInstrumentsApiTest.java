package com.trading;

import org.junit.Test;

public class RestInstrumentsApiTest {

    @Test(expected = UnsupportedOperationException.class)
    public void rejects_message_with_different_than_sedol_security_id_source() throws Exception {
        RestInstrumentsApi restInstrumentsApi = new RestInstrumentsApi();
        restInstrumentsApi.getInstrumentDetails("DUMMY", SecurityIDSource.CUSIP);
    }
}