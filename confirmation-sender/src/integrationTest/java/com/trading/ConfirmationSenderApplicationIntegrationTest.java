package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationSenderApplicationIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private BrokerService brokerService;

    private static final String DUMMY_ALLOCATION_ID = UUID.randomUUID().toString();

    @Before
    public void setUp() throws Exception {
        brokerService = new BrokerService();
        brokerService.addConnector("tcp://localhost:9999");
        brokerService.setPersistent(false);
        brokerService.start();
    }

    @After
    public void tearDown() throws Exception {
        brokerService.stop();
    }

    @Test
    public void consumes_incoming_message_and_send_confirmation() throws Exception {
        ConfirmationSenderApplication.main(new String[0]);

        AllocationReport allocationReport = TestData.allocationReport(DUMMY_ALLOCATION_ID);

        String allocationReportAsJson = objectMapper.writeValueAsString(allocationReport);

        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.send(
                queue(),
                session -> session.createTextMessage(allocationReportAsJson)
        );

        TimeUnit.SECONDS.sleep(5);

        Confirmation confirmation = FakeConfirmationSender.getConfirmation();

        allocationReport.setMessageStatus(MessageStatus.SENT);

        assertThat(confirmation.getAllocationReport())
                .isEqualToComparingFieldByField(allocationReport);
    }

    private String queue() {
        return "outgoing.allocation.report.queue";
    }

    private ConnectionFactory connectionFactory() {

        return new SingleConnectionFactory(
                new ActiveMQConnectionFactory("tcp://localhost:9999")
        );
    }
}
