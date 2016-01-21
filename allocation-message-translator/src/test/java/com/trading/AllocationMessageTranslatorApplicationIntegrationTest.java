package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.FileSystemUtils;

import javax.jms.ConnectionFactory;

import java.io.File;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AllocationMessageTranslatorApplicationIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private BrokerService brokerService;

    @Before
    public void setUp() throws Exception {
        FileSystemUtils.deleteRecursively(new File("activemq-data"));

        brokerService = new BrokerService();
        brokerService.addConnector("tcp://localhost:9999");
        brokerService.start();
    }

    @After
    public void tearDown() throws Exception {
        brokerService.stop();
        FileSystemUtils.deleteRecursively(new File("activemq-data"));
    }

    @Test
    public void consumes_incoming_message_and_sent_transfored_message_back_to_jms_server() throws Exception {
        AllocationMessageTranslatorApplication.main(new String[0]);

        String allocationReportId = UUID.randomUUID().toString();

        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.send(
                outgoingQueue(),
                session -> session.createTextMessage(String.format(TestData.FIXML_ALLOCATION_REPORT_MESSAGE, allocationReportId))
        );

        String message = (String) jmsTemplate.receiveAndConvert(incomingQueue());

        AllocationReport allocationReport = objectMapper.readValue(message, AllocationReport.class);

        AllocationReport expected = TestData.allocationReport();
        expected.setAllocationId(allocationReportId);
        assertThat(allocationReport).isEqualToComparingFieldByField(expected);
    }

    private String incomingQueue() {
        return "incoming.allocation.report.queue";
    }

    private String outgoingQueue() {
        return "front.office.mailbox";
    }

    private ConnectionFactory connectionFactory() {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:9999");

        SingleConnectionFactory factory = new SingleConnectionFactory();
        factory.setTargetConnectionFactory(activeMQConnectionFactory);
        return factory;
    }
}
