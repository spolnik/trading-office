package com.trading;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationRestServiceIntegrationTest {

    public static final String DUMMY_CONFIRMATION = "dummy confirmation";
    public static final String DUMMY_ALLOCATION_ID = "dummy";

    @Before
    public void setUp() throws Exception {
        ConfirmationServiceApplication.main(new String[0]);
    }

    @Test
    public void service_accepts_confirmation_and_then_allows_to_query_for_it() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:9003/api/confirmation", confirmation(), Object.class);

        Confirmation confirmation = restTemplate.getForObject(
                "http://localhost:9003/api/confirmation?id=" + confirmation().id(),
                Confirmation.class
        );

        assertThat(confirmation.id()).isEqualTo(DUMMY_ALLOCATION_ID);
        assertThat(confirmation.getContent()).isEqualTo(DUMMY_CONFIRMATION.getBytes());
    }

    private Confirmation confirmation() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId(DUMMY_ALLOCATION_ID);
        allocationReport.setTransactionType(TransactionType.NEW);

        Confirmation confirmation = new Confirmation();
        confirmation.setAllocationReport(allocationReport);
        confirmation.setContent(DUMMY_CONFIRMATION.getBytes());

        return confirmation;
    }
}
