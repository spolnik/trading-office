package com.trading;

import net.sf.jasperreports.engine.JRException;
import org.apache.activemq.ActiveMQConnectionFactory;
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
            ConfirmationSender confirmationSender, ConfirmationApi confirmationApi) throws JRException {

        return new ConfirmationMessageListener(
                confirmationSender,
                new EmailConfirmationParser(),
                new SwiftConfirmationParser(),
                confirmationApi);
    }

    @Bean
    ConfirmationSender confirmationSender() {
        return new ConfirmationServiceClient(confirmationServiceUrl);
    }

    @Bean
    ConfirmationApi confirmationApi() {
        return new ConfirmationApiClient(confirmationServiceUrl);
    }

}
