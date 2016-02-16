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
public class InstrumentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstrumentsApplication.class, args);
    }

    @Bean
    public Docket instrumentsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("instruments")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/api/instruments.*"))
                .build();
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Instruments REST Service")
                .description("Instruments REST Service")
                .contact("Jacek Spólnik")
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
    }
}
