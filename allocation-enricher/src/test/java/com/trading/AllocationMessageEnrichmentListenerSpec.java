package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AllocationMessageEnrichmentListenerSpec {

    @Test
    public void uses_allocation_report_enricher_for_message_enrichment() throws Exception {
        AllocationReportEnricher enricher = mock(AllocationReportEnricher.class);
        AllocationMessageEnrichmentListener listener = new AllocationMessageEnrichmentListener(enricher);

        listener.processAllocationReport(allocationReportAsJson());
        verify(enricher).process(any(AllocationReport.class));
    }

    private String allocationReportAsJson() throws JsonProcessingException {
        return objectMapper().toJson(TestData.allocationReport());
    }
}