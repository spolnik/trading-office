package com.trading;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.ConnectionFactory;

@RestController
@RequestMapping("/api")
public class AllocationReportController {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public AllocationReportController(ConnectionFactory connectionFactory) {
        this.jmsTemplate = new JmsTemplate(connectionFactory);
    }

    @ApiOperation(value = "processFixmlAllocationReport", nickname = "processFixmlAllocationReport", notes = "Process FIXML Allocation Report Message (text)")
    @RequestMapping(value = "allocation", method = RequestMethod.POST, consumes = "text/plain")
    public ResponseEntity processFixmlAllocationReport(
            @ApiParam(name = "fixmlMessage", required = true)
            @RequestBody(required = true) String fixmlMessage
    ) {
        jmsTemplate.send(
                Queues.INCOMING_FIXML_ALLOCATION_REPORT_QUEUE,
                session -> session.createTextMessage(fixmlMessage)
        );

        return ResponseEntity.ok().build();
    }
}
