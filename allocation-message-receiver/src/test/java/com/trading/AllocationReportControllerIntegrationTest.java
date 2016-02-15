package com.trading;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class AllocationReportControllerIntegrationTest {

    private static final String DUMMY = "DUMMY";

    private BrokerService brokerService;
    private AllocationReportController controller;

    @Before
    public void setUp() throws Exception {

        brokerService = new BrokerService();
        brokerService.addConnector("tcp://localhost:9998");
        brokerService.setPersistent(false);
        brokerService.start();

        controller = new AllocationReportController(connectionFactory());
    }

    @After
    public void tearDown() throws Exception {
        brokerService.stop();
    }

    @Test
    public void puts_the_message_on_incoming_queue() throws Exception {
        controller.processFixmlAllocationReport(DUMMY);

        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());

        String message = (String) jmsTemplate.receiveAndConvert(
                "incoming.fixml.allocation.report"
        );

        assertThat(message).isEqualTo(DUMMY);
    }

    private ConnectionFactory connectionFactory() {
        return new SingleConnectionFactory(
                new ActiveMQConnectionFactory("tcp://localhost:9998")
        );
    }
}