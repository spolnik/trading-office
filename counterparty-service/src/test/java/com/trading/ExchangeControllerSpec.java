package com.trading;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExchangeControllerSpec {

    private static final String NASDAQ_MIC_CODE = "XNAS";

    private ExchangeController controller;
    private Exchange exchange;
    private ExchangeRepository exchangeRepository;

    @Before
    public void setUp() throws Exception {
        exchange = mock(Exchange.class);

        exchangeRepository = mock(ExchangeRepository.class);
        when(exchangeRepository.getByMicCode(NASDAQ_MIC_CODE)).thenReturn(exchange);

        controller = new ExchangeController(exchangeRepository);
    }

    @Test
    public void returns_exchange_for_given_mic_code() throws Exception {

        Exchange nasdaq = controller.getExchange(NASDAQ_MIC_CODE);

        assertThat(nasdaq).isEqualTo(exchange);
    }

    @Test
    public void uses_exchange_repository_to_query_for_exchange() throws Exception {
        controller.getExchange(NASDAQ_MIC_CODE);

        verify(exchangeRepository).getByMicCode(NASDAQ_MIC_CODE);
    }
}