package com.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConfirmationServiceApplication {

    @Bean
    ConfirmationRepository confirmationRepository() {
        return new FileBasedConfirmationRepository();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfirmationServiceApplication.class, args);
    }
}
