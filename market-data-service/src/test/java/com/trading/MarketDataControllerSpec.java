package com.trading;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MarketDataControllerSpec {

    private static final String SYMBOL = "AMZN";

    @Test
    public void uses_yahoo_api_to_query_for_instrument() throws Exception {

        YahooApi yahooApi = mock(YahooApi.class);
        MarketDataController controller = new MarketDataController(yahooApi);

        controller.getInstrument(SYMBOL);

        verify(yahooApi).getInstrument(SYMBOL);
    }
}