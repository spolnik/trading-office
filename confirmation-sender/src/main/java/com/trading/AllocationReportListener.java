package com.trading;

import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@EventDriven
@Polling
@NotifyType(write = true, update = true)
@TransactionalEvent
public class AllocationReportListener {

    private static final Logger log = LoggerFactory.getLogger(AllocationReportListener.class);

    @EventTemplate
    AllocationReport unprocessedData() {
        AllocationReport template = new AllocationReport();
        template.setStatus("new");
        return template;
    }

    @SpaceDataEvent
    public AllocationReport eventListener(AllocationReport allocationReport) {
        log.info("Retrieved from cache: " + allocationReport);

        return null;
    }
}
