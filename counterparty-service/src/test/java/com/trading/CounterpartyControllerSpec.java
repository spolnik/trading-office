package com.trading;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CounterpartyControllerSpec {

    private CounterpartyController controller;

    @Before
    public void setUp() throws Exception {
        controller = new CounterpartyController();
    }

    @Test
    public void returns_Trading_Office_Ltd_counterparty_for_TROF_id() throws Exception {
        Counterparty counterparty = controller.getCounterparty("TROF");

        assertThat(counterparty.getName()).isEqualTo("Trading Office Ltd.");
    }

    @Test
    public void returns_Customer_UK_Ltd_counterparty_for_CUSTUK_id() throws Exception {
        Counterparty counterparty = controller.getCounterparty("CUSTUK");

        assertThat(counterparty.getName()).isEqualTo("Customer UK Ltd.");
    }
}