package com.weather.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "weather.api")
public class WeatherProperties {

    private String key;
    private String baseUrl;
    private String units;
    private String lang;
}
