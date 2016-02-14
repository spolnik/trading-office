package com.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
public class ConfirmationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfirmationServiceApplication.class, args);
    }

    @Bean
    ConfirmationRepository confirmationRepository() {
        return new FileBasedConfirmationRepository();
    }

    @Bean
    public Docket confirmaitonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("confirmation")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("(/api/confirmation.*)"))
                .build();
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Confirmation Data REST Service")
                .description("Confirmation Data REST Service")
                .contact("Jacek Spólnik")
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
    }
}
