package com.trading;

import com.j_spaces.core.LeaseContext;
import org.junit.Test;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

public class SpaceIntegrationTest {

    private static final String ALLOCATION_REPORT_ID = "1234";

    @Test
    public void can_save_and_retrieve_allocation_report_pojo() throws Exception {
        GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("/./test")).gigaSpace();

        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setAllocationId(ALLOCATION_REPORT_ID);
        allocationReport.setTradeType("BUY");

        gigaSpace.write(allocationReport);

        AllocationReport retrieved = gigaSpace.readById(AllocationReport.class, ALLOCATION_REPORT_ID);

        assertAllocationReport(retrieved);
    }

    private void assertAllocationReport(AllocationReport retrieved) {
        assertThat(retrieved.getAllocationId()).isEqualTo(ALLOCATION_REPORT_ID);
        assertThat(retrieved.getTradeType()).isEqualTo("BUY");
    }
}
