package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AllocationReportSpec {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void can_be_parsed_to_json_and_converted_back_to_object() throws Exception {

        String allocationReportAsJson = objectMapper.writeValueAsString(
                TestData.allocationReport()
        );

        AllocationReport allocationReportCreatedFromJson = objectMapper.readValue(
                allocationReportAsJson, AllocationReport.class
        );

        assertThat(allocationReportCreatedFromJson).isEqualToComparingFieldByField(
                TestData.allocationReport()
        );

        System.out.println(allocationReportCreatedFromJson);
    }
}
