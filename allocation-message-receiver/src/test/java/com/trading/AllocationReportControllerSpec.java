package com.trading;

import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AllocationReportControllerSpec {

    private RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private AllocationReportController controller;

    @Before
    public void setUp() throws Exception {

        controller = new AllocationReportController(rabbitTemplate);
    }

    @Test
    public void sends_incoming_message_by_rabbit_template() throws Exception {

        controller.processFixmlAllocationReport(TestData.FIXML_ALLOCATION_REPORT_MESSAGE);

        verify(rabbitTemplate).send(
                eq("trading-office-exchange"),
                eq("incoming.fixml.allocation.report"),
                any(Message.class)
        );
    }

    @Test
    public void returns_http_status_ok() throws Exception {

        ResponseEntity responseEntity = controller.processFixmlAllocationReport(
                TestData.FIXML_ALLOCATION_REPORT_MESSAGE
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}