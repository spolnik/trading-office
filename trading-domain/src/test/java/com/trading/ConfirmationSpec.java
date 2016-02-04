package com.trading;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationSpec {

    @Test
    public void can_be_parsed_to_json_and_converted_back_to_object() throws Exception {

        String confirmationAsString = objectMapper().toJson(confirmation());

        Confirmation confirmationCreatedFromJson = objectMapper().toConfirmation(
                confirmationAsString
        );

        assertThat(confirmationCreatedFromJson.getAllocationReport()).isEqualToComparingFieldByField(
                confirmation().getAllocationReport()
        );

        assertThat(confirmationCreatedFromJson.getContent()).isEqualTo(
                confirmation().getContent()
        );

        System.out.println(confirmationCreatedFromJson);
    }

    @Test
    public void has_id_derived_from_allocation_report() throws Exception {
        assertThat(confirmation().id()).isEqualTo(
                TestData.allocationReport().getAllocationId()
        );
    }

    private Confirmation confirmation() {
        Confirmation confirmation = new Confirmation();
        confirmation.setAllocationReport(TestData.allocationReport());
        confirmation.setContent("dummy confirmation content".getBytes(StandardCharsets.UTF_8));
        return confirmation;
    }
}
