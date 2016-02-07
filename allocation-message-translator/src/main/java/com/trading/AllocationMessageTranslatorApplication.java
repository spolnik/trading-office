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
class AllocationMessageTranslatorApplication {

    @Value("${activemqUrl}")
    private String activemqUrl;

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

    public static void main(String[] args) {
        SpringApplication.run(AllocationMessageTranslatorApplication.class, args);
    }
}
