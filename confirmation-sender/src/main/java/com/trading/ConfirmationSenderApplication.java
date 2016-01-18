package com.trading;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
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
public class ConfirmationSenderApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationSenderApplication.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ConfirmationSenderApplication.class);
        springApplication.run(args);
    }

    @Value("${activemqUrl}")
    private String activemqUrl;

    @Bean
    ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(activemqUrl);
    }

    @Bean
    JmsListenerContainerFactory<?> jmsContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        return factory;
    }

    @Override
    public void run(String... args) throws Exception {


        LOG.info("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();
    }

    @Bean
    public Sender<Confirmation> confirmationSender() {
        return new ConfirmationSender();
    }
}
