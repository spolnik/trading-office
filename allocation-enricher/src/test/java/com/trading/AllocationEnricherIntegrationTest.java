package com.trading;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.UUID;

import static com.trading.DomainObjectMapper.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocationEnricherIntegrationTest {

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

        String message = sendAndReceive(allocationReportAsJson());

        AllocationReport enrichedAllocationReport = fromJson(message);
        Instrument instrument = enrichedAllocationReport.getInstrument();

        assertThat(instrument).isEqualToIgnoringGivenFields(TestData.instrument(), "price");
        assertThat(enrichedAllocationReport.getExchange()).isEqualToComparingFieldByField(
                TestData.exchange()
        );
    }

    private String allocationReportAsJson() throws JsonProcessingException {
        String allocationReportId = UUID.randomUUID().toString();

        return objectMapper().toJson(
                allocationReportToEnrich(allocationReportId)
        );
    }

    private AllocationReport fromJson(String message) throws java.io.IOException {
        return objectMapper().toAllocationReport(message);
    }

    private String sendAndReceive(String allocationReportAsJson) {

        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());

        jmsTemplate.send(
                Queues.RECEIVED_JSON_ALLOCATION_REPORT_QUEUE,
                session -> session.createTextMessage(allocationReportAsJson)
        );

        return (String) jmsTemplate.receiveAndConvert(
                Queues.ENRICHED_JSON_ALLOCATION_REPORT_EMAIL_QUEUE
        );
    }

    private AllocationReport allocationReportToEnrich(String allocationReportId) {
        AllocationReport allocationReportToEnrich = TestData.allocationReport();
        allocationReportToEnrich.setAllocationId(allocationReportId);
        return allocationReportToEnrich;
    }

    private ConnectionFactory connectionFactory() {
        return new SingleConnectionFactory(
                new ActiveMQConnectionFactory("tcp://localhost:9999")
        );
    }
}
