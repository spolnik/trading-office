package com.trading;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String INCOMING_QUEUE = "received.json.allocation.report";

    @Value("${activemqUrl}")
    private String activemqUrl;

    @Value("${counterpartyServiceUrl}")
    private String counterpartyServiceUrl;

    @Value("${marketDataServiceUrl}")
    private String marketDataServiceUrl;

    @Bean
    ConnectionFactory connectionFactory() {

        return new CachingConnectionFactory(
                new ActiveMQConnectionFactory(activemqUrl)
        );
    }

    @Bean
    JmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
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
        return new InstrumentsApiClient(marketDataServiceUrl);
    }

    @Bean
    AllocationEnricher allocationEnricher(InstrumentsApi instrumentsApi, CounterpartyApi counterpartyApi) {
        return new AllocationEnricher(instrumentsApi, counterpartyApi);
    }

    public static void main(String[] args) {
        SpringApplication.run(AllocationEnricherApplication.class, args);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    Queue queue() {
        return new Queue(INCOMING_QUEUE, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("trading-office-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(INCOMING_QUEUE);
    }

    @Bean
    SimpleMessageListenerContainer container(
            org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(INCOMING_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    AllocationReceiver receiver(RabbitTemplate rabbitTemplate, AllocationEnricher allocationEnricher) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return new AllocationReceiver(rabbitTemplate, allocationEnricher);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(AllocationReceiver receiver) {
        return new MessageListenerAdapter(receiver, new Jackson2JsonMessageConverter());
    }
}
