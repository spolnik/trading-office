package com.trading;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

public class SpaceIntegrationTest {

    private static final String ALLOCATION_REPORT_ID = "1234";

    private UrlSpaceConfigurer configurer;
    private GigaSpace gigaSpace;

    @Before
    public void setUp() throws Exception {
        configurer = new UrlSpaceConfigurer("/./test");
        gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();
    }

    @After
    public void tearDown() throws Exception {
        configurer.close();
    }

    @Test
    public void can_save_and_retrieve_allocation_report_pojo() throws Exception {

        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setAllocationId(ALLOCATION_REPORT_ID);
        allocationReport.setTradeType("BUY");

        gigaSpace.write(allocationReport);
        assertThat(gigaSpace.count(new AllocationReport())).isEqualTo(1);

        AllocationReport retrieved = gigaSpace.takeById(AllocationReport.class, ALLOCATION_REPORT_ID);

        assertThat(gigaSpace.count(new AllocationReport())).isEqualTo(0);

        assertThat(retrieved.getAllocationId()).isEqualTo(ALLOCATION_REPORT_ID);
        assertThat(retrieved.getTradeType()).isEqualTo("BUY");
    }

}
