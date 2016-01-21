package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import javax.jms.ConnectionFactory;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationSenderApplicationIntegrationTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void consumes_incoming_message_and_send_confirmation() throws Exception {
        ConfirmationSenderApplication.main(new String[0]);

        AllocationReport allocationReport = TestData.allocationReport();

        String allocationReportAsJson = objectMapper.writeValueAsString(allocationReport);

        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.send(
                queue(),
                session -> session.createTextMessage(allocationReportAsJson)
        );

        TimeUnit.SECONDS.sleep(2);

        Confirmation confirmation = restTemplate.getForObject(
                "https://confirmation-service.herokuapp.com/api/confirmation?id="
                        + allocationReport.getAllocationId(),
                Confirmation.class
        );

        allocationReport.setMessageStatus(MessageStatus.SENT);
        assertThat(confirmation.getAllocationReport()).isEqualToComparingFieldByField(allocationReport);
    }

    private String queue() {
        return "incoming.allocation.report.queue";
    }

    private ConnectionFactory connectionFactory() {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("http://activemq-nprogramming.rhcloud.com");

        SingleConnectionFactory factory = new SingleConnectionFactory();
        factory.setTargetConnectionFactory(activeMQConnectionFactory);
        return factory;
    }
}
