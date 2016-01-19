package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReceivedAllocationReportMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(ReceivedAllocationReportMessageListener.class);

    private final JasperReport jasperReport;
    private final Sender<Confirmation> confirmationSender;
    private final ObjectMapper objectMapper;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    public ReceivedAllocationReportMessageListener(Sender<Confirmation> confirmationSender) throws JRException {
        this.confirmationSender = confirmationSender;
        InputStream resourceAsStream = ReceivedAllocationReportMessageListener.class
                .getClassLoader().getResourceAsStream("Confirmation.jrxml");

        jasperReport = JasperCompileManager.compileReport(resourceAsStream);
        objectMapper = new ObjectMapper();
    }

    @JmsListener(destination = "incoming.allocation.report.queue", containerFactory = "jmsContainerFactory")
    public void eventListener(String message) throws IOException {
        AllocationReport allocationReport = objectMapper.readValue(message, AllocationReport.class);
        LOG.info("Received: " + allocationReport);

        try {
            byte[] data = JasperRunManager.runReportToPdf(
                    jasperReport, parameters(allocationReport), new JREmptyDataSource()
            );

            allocationReport.setMessageStatus(MessageStatus.SENT);

            Confirmation confirmation = createConfirmationBasedOn(allocationReport, data);
            confirmationSender.send(confirmation);

        } catch (JRException e) {
            LOG.error(e.getMessage(), e);
        }
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
