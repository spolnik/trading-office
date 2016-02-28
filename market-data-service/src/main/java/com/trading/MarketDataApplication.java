package com.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import yahoofinance.Stock;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class MarketDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketDataApplication.class, args);
    }

    @Bean
    Converter<Stock, Instrument> stockInstrumentConverter() {
        return new StockToInstrumentConverter();
    }

    @Bean
    YahooApi yahooApi(Converter<Stock, Instrument> stockInstrumentConverter) {
        return new YahooApiClient(stockInstrumentConverter);
    }

    @Bean
    public Docket marketDataApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("market-data")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/api.*"))
                .build();
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Market Data REST Service")
                .description("Market Data REST Service")
                .contact("Jacek Spólnik")
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
    }
}
