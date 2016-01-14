package com.trading;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.util.FileSystemUtils;

import javax.jms.ConnectionFactory;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableJms
public class FrontOfficeWithJmsApplication {

    @Bean
    ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:9999");
    }

    @Bean
    JmsListenerContainerFactory<?> frontOfficeJmsContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        return factory;
    }

    public static void main(String[] args) throws Exception {

        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:9999");
        broker.start();

        SpringApplication.run(
                FrontOfficeWithJmsApplication.class, args
        );

        System.out.println("Press enter to close...");
        System.in.read();
        broker.stop();
    }
}
