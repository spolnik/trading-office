package com.trading;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationRestServiceIntegrationTest {

    @Before
    public void setUp() throws Exception {
        ConfirmationServiceApplication.main(new String[0]);
    }

    @Test
    public void service_accepts_confirmation_and_then_allows_to_query_for_it() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:9000/api/confirmation", confirmation(), Object.class);

        Confirmation confirmation = restTemplate.getForObject(
                "http://localhost:9000/api/confirmation?id=" + confirmation().id(),
                Confirmation.class
        );

        assertThat(confirmation.id()).isEqualTo("12345");
    }

    private Confirmation confirmation() {
        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setAllocationId("12345");
        allocationReport.setTransactionType(TransactionType.NEW);
        allocationReport.setMessageStatus(MessageStatus.SENT);

        Confirmation confirmation = new Confirmation();
        confirmation.setAllocationReport(allocationReport);
        confirmation.setContent("dummy confirmation".getBytes());

        return confirmation;
    }
}
