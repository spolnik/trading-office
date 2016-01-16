package com.trading;

import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class XapSpaceHostApp implements CommandLineRunner {

    private static final String GIGASPACES_URL = "/./tradingOffice";

    private static final Logger LOG = LoggerFactory.getLogger(XapSpaceHostApp.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(XapSpaceHostApp.class);
        springApplication.setWebEnvironment(false);

        Map<String, Object> settings = new HashMap<>();
        settings.put("spring.jta.enabled", false);
        springApplication.setDefaultProperties(settings);

        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        UrlSpaceConfigurer configurer = new UrlSpaceConfigurer(GIGASPACES_URL);
        new GigaSpaceConfigurer(configurer).gigaSpace();
        LOG.info("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();

        configurer.close();
    }
}
