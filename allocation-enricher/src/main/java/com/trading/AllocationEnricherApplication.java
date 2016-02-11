package com.trading;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
@PropertySource("classpath:app.properties")
public class AllocationEnricherApplication {

    @Value("${activemqUrl}")
    private String activemqUrl;

    @Value("${counterpartyServiceUrl}")
    private String counterpartyServiceUrl;

    @Value("${instrumentServiceUrl}")
    private String instrumentServiceUrl;

    @Value("${financeDataServiceUrl}")
    private String financeDataServiceUrl;

    @Bean
    ConnectionFactory connectionFactory() {

        return new CachingConnectionFactory(
                new ActiveMQConnectionFactory(activemqUrl)
        );
    }

    @Bean
    JmsListenerContainerFactory jmsContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        return factory;
    }

    @Bean
    CounterpartyApi counterpartyApi() {
        return new CounterpartyApiClient(counterpartyServiceUrl);
    }

    @Bean
    InstrumentsApi instrumentsApi() {
        return new InstrumentsApiClient(instrumentServiceUrl, financeDataServiceUrl);
    }

    @Bean
    AllocationReportEnricher allocationReportEnricher(InstrumentsApi instrumentsApi, CounterpartyApi counterpartyApi) {
        return new AllocationReportEnricher(instrumentsApi, counterpartyApi);
    }

    public static void main(String[] args) {
        SpringApplication.run(AllocationEnricherApplication.class, args);
    }
}
