package com.trading;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AllocationStatusSpec {

    @Test(expected = UnsupportedOperationException.class)
    public void throws_exception_for_unsupported_id() throws Exception {
        AllocationStatus.getAllocationStatus("1");
    }

    @Test
    public void returns_accepted_for_id_0() throws Exception {
        assertThat(AllocationStatus.getAllocationStatus("0")).isEqualTo(AllocationStatus.ACCEPTED);
    }

    @Test
    public void returns_accepted_for_id_3() throws Exception {
        assertThat(AllocationStatus.getAllocationStatus("3")).isEqualTo(AllocationStatus.RECEIVED);
    }

    @Test
    public void returns_accepted_for_id_6() throws Exception {
        assertThat(AllocationStatus.getAllocationStatus("6")).isEqualTo(AllocationStatus.ALLOCATION_PENDING);
    }
}