package com.trading;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationServiceClientIntegrationTest {

    private static final String CONFIRMATION_SERVICE_URL = "http://confirmation-service.herokuapp.com";
    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void is_able_to_send_confirmation_and_query_for_it() throws Exception {
        ConfirmationServiceClient client = new ConfirmationServiceClient(CONFIRMATION_SERVICE_URL);

        Confirmation confirmationToSave = TestData.confirmation("DUMMY_ID_123");
        confirmationToSave.setContent("DUMMY".getBytes());
        confirmationToSave.setConfirmationType(Confirmation.SWIFT);
        client.send(confirmationToSave);

        Confirmation confirmation = restTemplate.getForObject(
                CONFIRMATION_SERVICE_URL + "/api/counterparty/" + confirmationToSave.getAllocationId(),
                Confirmation.class
        );

        assertThat(confirmation).isEqualToComparingFieldByField(confirmationToSave);
    }
}