package com.trading;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class AllocationReportController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AllocationReportController(ConnectionFactory connectionFactory) {
        this.rabbitTemplate = new RabbitTemplate(connectionFactory);
    }

    @ApiOperation(value = "processFixmlAllocationReport", nickname = "processFixmlAllocationReport", notes = "Process FIXML Allocation Report Message (text)")
    @RequestMapping(value = "allocation", method = RequestMethod.POST, consumes = "text/plain")
    public ResponseEntity processFixmlAllocationReport(
            @ApiParam(name = "fixmlAllocationMessage", required = true)
            @RequestBody(required = true) String fixmlAllocationMessage
    ) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message(fixmlAllocationMessage.getBytes(StandardCharsets.UTF_8), messageProperties);

        rabbitTemplate.send(
                "trading-office-exchange",
                "incoming.fixml.allocation.report",
                message
        );

        return ResponseEntity.ok().build();
    }
}