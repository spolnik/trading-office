package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AllocationMessageEnrichmentListenerSpec {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void uses_allocation_report_enricher_for_message_enrichment() throws Exception {
        AllocationReportEnricher enricher = mock(AllocationReportEnricher.class);
        AllocationMessageEnrichmentListener listener = new AllocationMessageEnrichmentListener(enricher);

        listener.processAllocationReport(allocationReportAsJson());
        verify(enricher).process(any(EnrichedAllocation.class));
    }

    private String allocationReportAsJson() throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(TestData.allocationReport());
    }
}