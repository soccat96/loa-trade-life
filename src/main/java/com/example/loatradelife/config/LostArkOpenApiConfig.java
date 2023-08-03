package com.example.loatradelife.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "loa-open-api")
@Data
public class LostArkOpenApiConfig {
    private String key;
    private String baseUrl;
}
