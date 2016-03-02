package com.trading;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
public class RabbitMqConfig {

    private static final String INCOMING_QUEUE = "received.json.allocation.report";

    @Bean
    ConnectionFactory connectionFactory() throws URISyntaxException {

        String uri = System.getenv("CLOUDAMQP_URL");

        if (uri == null) {
            uri = "amqp://guest:guest@localhost";
        }

        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(uri);
        factory.setRequestedHeartBeat(30);
        factory.setConnectionTimeout(30);

        return factory;
    }

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
    MessageListenerAdapter listenerAdapter(AllocationReceiver receiver) {
        return new MessageListenerAdapter(receiver, new Jackson2JsonMessageConverter());
    }
}
