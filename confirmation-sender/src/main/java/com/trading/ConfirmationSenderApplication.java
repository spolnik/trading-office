package com.trading;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
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

    private static final String GIGASPACES_URL ="jini://*/*/tradingOffice";

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationSenderApplication.class);

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
        GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer(GIGASPACES_URL)).gigaSpace();

        SimplePollingEventListenerContainer pollingListener = new SimplePollingContainerConfigurer(gigaSpace)
                .template(new AllocationReport())
                .eventListenerAnnotation(new ReceivedAllocationReportListener(confirmationSender()))
                .pollingContainer();

        pollingListener.start();

        LOG.info("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();

        pollingListener.stop();
    }

    private Sender<Confirmation> confirmationSender() {
        return new ConfirmationSender();
    }
}
