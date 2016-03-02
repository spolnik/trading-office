package com.trading;

import net.sf.jasperreports.engine.JRException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class ConfirmationSenderApplication {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ConfirmationClient confirmationClient;

    public static void main(String[] args) {
        SpringApplication.run(ConfirmationSenderApplication.class, args);
    }
    
    @Bean
    EnrichedAllocationReceiver receiver() throws JRException {

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        return new EnrichedAllocationReceiver(
                confirmationClient,
                new EmailConfirmationParser(),
                new SwiftConfirmationParser()
        );
    }
}
