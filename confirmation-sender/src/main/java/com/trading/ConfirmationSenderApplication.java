package com.trading;

import net.sf.jasperreports.engine.JRException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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

import java.net.URISyntaxException;

@SpringBootApplication
@PropertySource("classpath:app.properties")
public class ConfirmationSenderApplication {

    private static final String INCOMING_QUEUE = "enriched.json.allocation.report";

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${confirmationServiceUrl}")
    private String confirmationServiceUrl;

    public static void main(String[] args) {
        SpringApplication.run(ConfirmationSenderApplication.class, args);
    }

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
    ConfirmationSender confirmationSender() {
        return new ConfirmationServiceClient(confirmationServiceUrl);
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
    EnrichedAllocationReceiver receiver(ConfirmationSender confirmationSender) throws JRException {

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        return new EnrichedAllocationReceiver(
                confirmationSender,
                new EmailConfirmationParser(),
                new SwiftConfirmationParser()
        );
    }

    @Bean
    MessageListenerAdapter listenerAdapter(EnrichedAllocationReceiver receiver) {
        return new MessageListenerAdapter(receiver, new Jackson2JsonMessageConverter());
    }
}
