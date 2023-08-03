package com.example.loatradelife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LoaTradeLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoaTradeLifeApplication.class, args);
    }
}