package com.trading;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CounterpartyControllerSpec {

    private static final String TRADING_OFFICE_LTD_PARTY_NAME = "Trading Office Ltd.";
    private static final String CUSTOMER_UK_LTD_PARTY_NAME = "Customer UK Ltd.";
    private static final String TROF_PARTY_ID = "TROF";
    private static final String CUSTUK_PARTY_ID = "CUSTUK";

    private CounterpartyController controller;
    private PartyRepository partyRepository;

    @Before
    public void setUp() throws Exception {

        partyRepository = mock(PartyRepository.class);

        when(partyRepository.getName(TROF_PARTY_ID)).thenReturn(TRADING_OFFICE_LTD_PARTY_NAME);
        when(partyRepository.getName(CUSTUK_PARTY_ID)).thenReturn(CUSTOMER_UK_LTD_PARTY_NAME);

        controller = new CounterpartyController(partyRepository);
    }

    @Test
    public void returns_Trading_Office_Ltd_counterparty_for_TROF_id() throws Exception {

        String partyName = controller.getPartyName(TROF_PARTY_ID);

        assertThat(partyName).isEqualTo(TRADING_OFFICE_LTD_PARTY_NAME);
    }

    @Test
    public void returns_Customer_UK_Ltd_counterparty_for_CUSTUK_id() throws Exception {
        String partyName = controller.getPartyName(CUSTUK_PARTY_ID);

        assertThat(partyName).isEqualTo(CUSTOMER_UK_LTD_PARTY_NAME);
    }

    @Test
    public void uses_party_repository_to_query_for_exchange() throws Exception {

        String dummyId = "DUMMY";

        controller.getPartyName(dummyId);

        verify(partyRepository).getName(dummyId);
    }
}