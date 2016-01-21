package com.trading;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FixmlMessageParserSpec {

    private static AllocationReport allocationReport;

    private static final String ALLOCATION_REPORT_ID = UUID.randomUUID().toString();

    @BeforeClass
    public static void setUp() throws Exception {
        FixmlMessageParser parser = new FixmlMessageParser();
        allocationReport = parser.parse(String.format(TestData.FIXML_ALLOCATION_REPORT_MESSAGE, ALLOCATION_REPORT_ID));
    }

    @Test
    public void parses_allocation_id() throws Exception {
        assertThat(allocationReport.getAllocationId()).isEqualTo(ALLOCATION_REPORT_ID);
    }

    @Test
    public void parses_transaction_type() throws Exception {
        assertThat(allocationReport.getTransactionType()).isEqualTo(TransactionType.NEW);
    }

    @Test
    public void parses_security_id() throws Exception {
        assertThat(allocationReport.getSecurityId()).isEqualTo("2000019");
    }

    @Test
    public void parses_instrument_id_source() throws Exception {
        assertThat(allocationReport.getSecurityIdSource()).isEqualTo(SecurityIDSource.SEDOL);
    }
}
