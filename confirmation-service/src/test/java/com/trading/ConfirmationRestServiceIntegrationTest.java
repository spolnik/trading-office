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
                "http://localhost:9003/api/confirmation/" + confirmation().id(),
                Confirmation.class
        );

        assertThat(confirmation.id()).isEqualTo(DUMMY_ALLOCATION_ID);
        assertThat(confirmation.getContent()).isEqualTo(DUMMY_CONFIRMATION.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void returns_email_confirmation_type_for_exchange_other_than_london_stack_exchange() throws Exception {

        ConfirmationType confirmationType = restTemplate.getForObject(
                "http://localhost:9003/api/confirmation/type/XNAS",
                ConfirmationType.class
        );

        assertThat(confirmationType).isEqualTo(ConfirmationType.EMAIL);
    }

    @Test
    public void returns_swift_confirmation_type_for_london_stack_exchange() throws Exception {
        ConfirmationType confirmationType = restTemplate.getForObject(
                "http://localhost:9003/api/confirmation/type/XLON",
                ConfirmationType.class
        );

        assertThat(confirmationType).isEqualTo(ConfirmationType.SWIFT);
    }

    private Confirmation confirmation() {
        AllocationReport allocationReport = new AllocationReport();

        allocationReport.setAllocationId(DUMMY_ALLOCATION_ID);
        allocationReport.setTransactionType(TransactionType.NEW);

        Confirmation confirmation = new Confirmation();
        confirmation.setAllocationReport(allocationReport);
        confirmation.setContent(DUMMY_CONFIRMATION.getBytes(StandardCharsets.UTF_8));

        return confirmation;
    }
}
