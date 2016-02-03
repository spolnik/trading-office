package com.trading;

import org.junit.Test;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocationReportSpec {

    @Test
    public void can_be_parsed_to_json_and_converted_back_to_object() throws Exception {

        String allocationReportAsJson = objectMapper().toJson(
                TestData.allocationReport()
        );

        AllocationReport allocationReportCreatedFromJson = objectMapper().toAllocationReport(
                allocationReportAsJson
        );

        assertThat(allocationReportCreatedFromJson).isEqualToComparingFieldByField(
                TestData.allocationReport()
        );

        System.out.println(allocationReportCreatedFromJson);
    }
}
