package com.weather.controller;

import com.weather.model.WeatherData;
import com.weather.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String city,
            Model model) {

        if (city != null && !city.isBlank()) {
            try {
                WeatherData weatherData = weatherService.getWeather(city);
                model.addAttribute("weather", weatherData);
                model.addAttribute("searchedCity", city);
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("searchedCity", city);
            }
        }

        return "index";
    }
}
