package com.trading;

import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class JmsHostApplication implements CommandLineRunner {

    private static final BrokerService BROKER_SERVICE = new BrokerService();

    private static final Logger LOG = LoggerFactory.getLogger(JmsHostApplication.class);

    public static void main(String[] args) throws Exception {

        BROKER_SERVICE.addConnector("tcp://localhost:9999");
        BROKER_SERVICE.start();

        SpringApplication.run(
                JmsHostApplication.class, args
        );
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();
        BROKER_SERVICE.stop();
    }
}
