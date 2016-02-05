package com.trading;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeControllerSpec {

    private ExchangeController controller;

    @Before
    public void setUp() throws Exception {
        controller = new ExchangeController();
    }

    @Test
    public void returns_correctly_name_for_nasdaq_mic_code() throws Exception {
        Exchange nasdaq = controller.getExchange("XNAS");

        assertThat(nasdaq.getName()).isEqualTo("NASDAQ - ALL MARKETS");
    }

    @Test
    public void returns_correctly_acronym_for_nasdaq_mic_code() throws Exception {
        Exchange nasdaq = controller.getExchange("XNAS");

        assertThat(nasdaq.getAcronym()).isEqualTo("NASDAQ");
    }
}