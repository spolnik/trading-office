package com.trading;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationServiceClientIntegrationTest {

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void is_able_to_send_confirmation_and_query_for_it() throws Exception {
        ConfirmationServiceClient client = new ConfirmationServiceClient(
                new RestTemplate(),
                "confirmation-service.herokuapp.com"
        );

        Confirmation confirmationToSave = TestData.confirmation("DUMMY_ID_123");
        confirmationToSave.setContent("DUMMY".getBytes());
        confirmationToSave.setConfirmationType(Confirmation.SWIFT);
        client.send(confirmationToSave);

        Confirmation confirmation = restTemplate.getForObject(
                "http://confirmation-service.herokuapp.com/api/confirmation/" + confirmationToSave.getAllocationId(),
                Confirmation.class
        );

        assertThat(confirmation).isEqualToComparingFieldByField(confirmationToSave);
    }
}