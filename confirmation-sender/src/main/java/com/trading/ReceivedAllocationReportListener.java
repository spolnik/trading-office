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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
@EventDriven
@Polling
@NotifyType(write = true, update = true)
@TransactionalEvent
public class ReceivedAllocationReportListener {

    private static final Logger LOG = LoggerFactory.getLogger(ReceivedAllocationReportListener.class);

    private final JasperReport jasperReport;
    private final Sender<Confirmation> confirmationSender;

    public ReceivedAllocationReportListener(Sender<Confirmation> confirmationSender) throws JRException {
        this.confirmationSender = confirmationSender;
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
    public void eventListener(AllocationReport allocationReport, GigaSpace space) {
        LOG.info("Retrieved from cache: " + allocationReport);

        try {
            byte[] data = JasperRunManager.runReportToPdf(
                    jasperReport, parameters(allocationReport), new JREmptyDataSource()
            );

            updateStateOfAllocationAndSaveIntoGigaspaces(allocationReport, space);

            Confirmation confirmation = createConfirmationBasedOn(allocationReport, data);
            confirmationSender.send(confirmation);

        } catch (JRException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void updateStateOfAllocationAndSaveIntoGigaspaces(AllocationReport allocationReport, GigaSpace space) {
        allocationReport.setMessageStatus(MessageStatus.SENT);
        space.write(allocationReport);
    }

    private Confirmation createConfirmationBasedOn(AllocationReport allocationReport, byte[] data) {
        Confirmation confirmation = new Confirmation();
        confirmation.setAllocationReport(allocationReport);
        confirmation.setContent(data);
        return confirmation;
    }

    private Map<String, Object> parameters(AllocationReport allocationReport) {
        Map<String, Object> map = new HashMap<>();
        map.put("ALLOC_RPT_ID", allocationReport.getAllocationId());
        map.put("TRANS_TYPE", allocationReport.getTransactionType().toString());
        return map;
    }
}
