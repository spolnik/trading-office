package com.trading;

import org.apache.activemq.broker.BrokerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class FrontOfficeWithJmsApplication {

    private static final BrokerService broker = new BrokerService();

    public static void main(String[] args) throws Exception {

        start(args);

        System.out.println("Press enter to close...");
        System.in.read();

        stop();
    }

    public static void stop() throws Exception {
        broker.stop();
    }

    public static BrokerService start(String[] args) throws Exception {

        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:9999");
        broker.start();

        SpringApplication.run(
                FrontOfficeWithJmsApplication.class, args
        );

        return broker;
    }


}
