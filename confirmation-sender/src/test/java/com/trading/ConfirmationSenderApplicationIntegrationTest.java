package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration
public class ConfirmationSenderApplicationIntegrationTest {

    private BrokerService brokerService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

        Confirmation allocationReport = TestData.allocationReport(DUMMY_ALLOCATION_ID);

        String allocationReportAsJson = OBJECT_MAPPER.writeValueAsString(allocationReport);

        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.send(
                "enriched.json.allocation.report",
                session -> session.createTextMessage(allocationReportAsJson)
        );

        TimeUnit.SECONDS.sleep(10);

        Confirmation confirmation = FakeConfirmationSender.getConfirmation();

        assertThat(confirmation).isEqualToIgnoringGivenFields(allocationReport, "content", "confirmationType");
    }

    private ConnectionFactory connectionFactory() {

        return new SingleConnectionFactory(
                new ActiveMQConnectionFactory("tcp://localhost:9999")
        );
    }
}
