package com.trading;

import java.util.Arrays;
import java.util.Optional;

public enum AllocationStatus {
    ACCEPTED("0"),
    RECEIVED("3"),
    ALLOCATION_PENDING("6");

    private final String fixmlAllocationStatus;

    AllocationStatus(String fixmlAllocationStatus) {
        this.fixmlAllocationStatus = fixmlAllocationStatus;
    }

    public static AllocationStatus getAllocationStatus(String fixmlAllocationStatus) {
        Optional<AllocationStatus> allocationStatus = Arrays.asList(values())
                .stream()
                .filter(value -> value.fixmlAllocationStatus.equals(fixmlAllocationStatus))
                .findFirst();

        return allocationStatus.orElseThrow(
                () -> new UnsupportedOperationException("Allocation Status is unsupported: " + fixmlAllocationStatus)
        );
    }
}
