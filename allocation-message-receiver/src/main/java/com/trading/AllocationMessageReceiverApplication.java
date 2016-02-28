package com.trading;

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
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.URISyntaxException;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
public class AllocationMessageReceiverApplication {

    private static final String INCOMING_QUEUE = "incoming.fixml.allocation.report";

    public static void main(String[] args) {
        SpringApplication.run(AllocationMessageReceiverApplication.class, args);
    }

    @Bean
    FixmlMessageParser fixmlMessageParser() {
        return new FixmlMessageParser();
    }

    @Bean
    public ConnectionFactory connectionFactory() throws URISyntaxException {

        String uri = System.getenv("CLOUDAMQP_URL");
        if (uri == null) uri = "amqp://guest:guest@localhost";

        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(uri);
        factory.setRequestedHeartBeat(30);
        factory.setConnectionTimeout(30);

        return factory;
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
    FixmlAllocationMessageReceiver receiver(RabbitTemplate rabbitTemplate, FixmlMessageParser parser) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return new FixmlAllocationMessageReceiver(rabbitTemplate, parser);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(FixmlAllocationMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, new SimpleMessageConverter());
    }

    @Bean
    public Docket allocationMessageReceiverApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("allocation")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("(/api/allocation.*)"))
                .build();
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Allocation Message Receiver REST Service")
                .description("Allocation Message Receiver REST Service")
                .contact("Jacek Spólnik")
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
    }
}
