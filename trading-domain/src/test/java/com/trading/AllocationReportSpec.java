package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AllocationReportSpec {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void can_be_parsed_to_json_and_converted_back_to_object() throws Exception {

        String allocationReportAsJson = objectMapper.writeValueAsString(
                allocationReport()
        );

        AllocationReport allocationReportCreatedFromJson = objectMapper.readValue(
                allocationReportAsJson, AllocationReport.class
        );

        assertThat(allocationReportCreatedFromJson).isEqualToComparingFieldByField(
                allocationReport()
        );
    }

    private AllocationReport allocationReport() {
        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setAllocationId("12345");
        allocationReport.setMessageStatus(MessageStatus.NEW);
        allocationReport.setTransactionType(TransactionType.NEW);
        return allocationReport;
    }
}
