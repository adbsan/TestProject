package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherData {

    private String cityName;
    private double temperature;
    private String description;
    private int humidity;
    private double windSpeed;
    private String iconUrl;
}
