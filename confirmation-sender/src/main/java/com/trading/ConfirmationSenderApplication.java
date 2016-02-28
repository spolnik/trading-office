package com.trading;

import net.sf.jasperreports.engine.JRException;
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

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
@PropertySource("classpath:app.properties")
public class ConfirmationSenderApplication {

    private static final String INCOMING_QUEUE = "enriched.json.allocation.report";

    @Value("${activemqUrl}")
    private String activemqUrl;

    @Value("${confirmationServiceUrl}")
    private String confirmationServiceUrl;

    public static void main(String[] args) {
        SpringApplication.run(ConfirmationSenderApplication.class, args);
    }

    @Bean
    ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(activemqUrl);
    }

    @Bean
    JmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        return factory;
    }

    @Bean
    ConfirmationMessageListener confirmationMessageListener(
            ConfirmationSender confirmationSender) throws JRException {

        return new ConfirmationMessageListener(
                confirmationSender,
                new EmailConfirmationParser(),
                new SwiftConfirmationParser()
        );
    }

    @Bean
    ConfirmationSender confirmationSender() {
        return new ConfirmationServiceClient(confirmationServiceUrl);
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
