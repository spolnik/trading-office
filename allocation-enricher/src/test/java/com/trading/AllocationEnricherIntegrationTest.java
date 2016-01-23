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

import static org.assertj.core.api.Assertions.assertThat;

public class AllocationEnricherIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private BrokerService brokerService;

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
    public void consumes_incoming_message_and_sent_transformed_message_back_to_jms_server() throws Exception {
        AllocationEnricherApplication.main(new String[0]);

        String allocationReportId = UUID.randomUUID().toString();

        AllocationReport allocationReportToEnrich = TestData.allocationReport();
        allocationReportToEnrich.setAllocationId(allocationReportId);

        String allocationReportAsJson = objectMapper.writeValueAsString(allocationReportToEnrich);

        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.send(
                incomingQueue(),
                session -> session.createTextMessage(allocationReportAsJson)
        );

        String message = (String) jmsTemplate.receiveAndConvert(destinationQueue());

        AllocationReport enrichedAllocationReport = objectMapper.readValue(message, AllocationReport.class);
        Instrument instrument = enrichedAllocationReport.getInstrument();

        assertThat(instrument).isEqualToIgnoringGivenFields(TestData.instrument(), "price");
    }

    private String destinationQueue() {
        return "outgoing.allocation.report.queue";
    }

    private String incomingQueue() {
        return "incoming.allocation.report.queue";
    }

    private ConnectionFactory connectionFactory() {
        return new SingleConnectionFactory(
                new ActiveMQConnectionFactory("tcp://localhost:9999")
        );
    }
}
