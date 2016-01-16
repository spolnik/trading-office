package com.trading;

import com.google.common.io.Resources;
import net.sf.jasperreports.engine.*;
import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
@EventDriven
@Polling
@NotifyType(write = true, update = true)
@TransactionalEvent
public class ReceivedAllocationReportListener {

    private static final Logger log = LoggerFactory.getLogger(ReceivedAllocationReportListener.class);

    private final JasperReport jasperReport;

    public ReceivedAllocationReportListener() throws JRException {
        URL jrxmlTemplate = Resources.getResource("Confirmation.jrxml");

        jasperReport = JasperCompileManager.compileReport(jrxmlTemplate.getFile());
    }

    @EventTemplate
    AllocationReport unprocessedData() {
        AllocationReport template = new AllocationReport();
        template.setMessageStatus(MessageStatus.NEW);
        return template;
    }

    @SpaceDataEvent
    public void eventListener(AllocationReport allocationReport, GigaSpace space) throws IOException, JRException {
        log.info("Retrieved from cache: " + allocationReport);

        byte[] data = JasperRunManager.runReportToPdf(
                jasperReport, parameters(allocationReport), new JREmptyDataSource()
        );

        Path confirmationpath = Files.write(Paths.get("Confirmation.pdf"), data);
        log.info("Confirmation PDF saved: " + confirmationpath);

        allocationReport.setMessageStatus(MessageStatus.SENT);
        space.write(allocationReport);
    }

    private Map<String, Object> parameters(AllocationReport allocationReport) {
        Map<String, Object> map = new HashMap<>();
        map.put("ALLOC_RPT_ID", allocationReport.getAllocationId());
        map.put("TRANS_TYPE", allocationReport.getTransactionType().toString());
        return map;
    }
}
