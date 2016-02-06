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
        Party party = controller.getCounterparty("TROF");

        assertThat(party.getName()).isEqualTo("Trading Office Ltd.");
    }

    @Test
    public void returns_Customer_UK_Ltd_counterparty_for_CUSTUK_id() throws Exception {
        Party party = controller.getCounterparty("CUSTUK");

        assertThat(party.getName()).isEqualTo("Customer UK Ltd.");
    }
}