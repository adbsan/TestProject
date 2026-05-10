package com.weather.service;

import com.weather.config.WeatherProperties;
import com.weather.model.WeatherData;
import com.weather.model.WeatherResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private final WeatherProperties props;
    private RestClient restClient;

    public WeatherService(WeatherProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void init() {
        this.restClient = RestClient.builder().build();
    }

    public WeatherData getWeather(String city) {
        String url = UriComponentsBuilder.fromUriString(props.getBaseUrl())
                .queryParam("q", city)
                .queryParam("appid", props.getKey())
                .queryParam("units", props.getUnits())
                .queryParam("lang", props.getLang())
                .toUriString();

        try {
            WeatherResponse response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(WeatherResponse.class);

            if (response == null || response.getWeather() == null || response.getWeather().isEmpty()) {
                throw new RuntimeException("天気データを取得できませんでした。");
            }

            String iconCode = response.getWeather().get(0).getIcon();
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

            return new WeatherData(
                    response.getName(),
                    response.getMain().getTemp(),
                    response.getWeather().get(0).getDescription(),
                    response.getMain().getHumidity(),
                    response.getWind().getSpeed(),
                    iconUrl
            );

        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("都市「" + city + "」が見つかりませんでした。スペルを確認してください。");
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("APIキーが無効です。application.properties を確認してください。");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("天気情報の取得に失敗しました: " + e.getMessage(), e);
        }
    }
}
