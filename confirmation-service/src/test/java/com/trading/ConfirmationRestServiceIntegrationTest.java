package com.trading;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationRestServiceIntegrationTest {

    private static final String DUMMY_CONFIRMATION = "dummy confirmation";
    private static final String DUMMY_ALLOCATION_ID = "dummy";
    private RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void setUp() throws Exception {
        ConfirmationServiceApplication.main(new String[0]);
    }

    @Test
    public void saves_confirmation_and_then_allows_to_query_for_it() throws Exception {

        restTemplate.postForObject("http://localhost:9003/api/confirmation", confirmation(), Object.class);

        Confirmation confirmation = restTemplate.getForObject(
                "http://localhost:9003/api/confirmation/" + confirmation().getAllocationId(),
                Confirmation.class
        );

        assertThat(confirmation).isEqualToComparingFieldByField(confirmation());
    }

    private Confirmation confirmation() {

        Confirmation confirmation = new Confirmation();
        confirmation.setAllocationId(DUMMY_ALLOCATION_ID);
        confirmation.setConfirmationType(Confirmation.EMAIL);
        confirmation.setContent(DUMMY_CONFIRMATION.getBytes(StandardCharsets.UTF_8));

        return confirmation;
    }
}
