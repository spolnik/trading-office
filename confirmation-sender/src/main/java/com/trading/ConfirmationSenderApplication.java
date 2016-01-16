package com.trading;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.openspaces.events.notify.SimpleNotifyContainerConfigurer;
import org.openspaces.events.notify.SimpleNotifyEventListenerContainer;
import org.openspaces.events.polling.SimplePollingContainerConfigurer;
import org.openspaces.events.polling.SimplePollingEventListenerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ConfirmationSenderApplication implements CommandLineRunner {

    private static final String gigaspaceUrl ="jini://*/*/tradingOffice";

    private static final Logger log = LoggerFactory.getLogger(ConfirmationSenderApplication.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ConfirmationSenderApplication.class);
        springApplication.setWebEnvironment(false);

        Map<String, Object> settings = new HashMap<>();
        settings.put("spring.jta.enabled", false);
        springApplication.setDefaultProperties(settings);

        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer(gigaspaceUrl)).gigaSpace();

        SimpleNotifyEventListenerContainer notifyListener = new SimpleNotifyContainerConfigurer(gigaSpace)
                .template(new AllocationReport())
                .eventListenerAnnotation(new ReceivedAllocationReportListener())
                .notifyContainer();

        notifyListener.start();

        log.info("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();

        notifyListener.stop();
    }
}
