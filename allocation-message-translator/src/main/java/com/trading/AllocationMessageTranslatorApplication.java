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
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.jms.ConnectionFactory;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableJms
@EnableSwagger2
@PropertySource("classpath:app.properties")
public class AllocationMessageTranslatorApplication {

    @Value("${activemqUrl}")
    private String activemqUrl;

    public static void main(String[] args) {
        SpringApplication.run(AllocationMessageTranslatorApplication.class, args);
    }

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
