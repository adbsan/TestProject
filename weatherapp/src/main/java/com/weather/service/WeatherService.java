package com.weather.service;

import com.weather.config.WeatherProperties;
import com.weather.model.WeatherData;
import com.weather.model.WeatherResponse;
import jakarta.annotation.PostConstruct; // 起動時に自動で実行されるメソッドにつけるアノテーション
import org.springframework.stereotype.Service; // Springが管理するサービスクラスとして登録
import org.springframework.web.client.HttpClientErrorException; // HTTPエラーを扱うクラス
import org.springframework.web.client.RestClient; // APIにHTTPリクエストを送るクラス
import org.springframework.web.util.UriComponentsBuilder; // URLを組み立てるクラス

// OpenWeather APIを呼び出して天気データを取得するクラス
@Service
public class WeatherService {

    private final WeatherProperties props; // application.propertiesの設定値
    private RestClient restClient;         // APIにリクエストを送る道具

    // コンストラクタ：WeatherPropertiesを受け取って使えるようにする
    public WeatherService(WeatherProperties props) {
        this.props = props;
    }

    // アプリ起動時に一度だけ実行：RestClientを準備する
    @PostConstruct
    public void init() {
        this.restClient = RestClient.builder().build();
    }

    // 都市名を受け取りAPIに問い合わせ、WeatherDataに変換して返すメソッド
    public WeatherData getWeather(String city) {

        // APIに送るURLを組み立てる
        // 例：https://api.openweathermap.org/data/2.5/weather?q=Tokyo&appid=xxx&units=metric&lang=ja
        String url = UriComponentsBuilder.fromUriString(props.getBaseUrl())
                .queryParam("q", city)           // 都市名
                .queryParam("appid", props.getKey()) // APIキー
                .queryParam("units", props.getUnits()) // 単位（metric=摂氏）
                .queryParam("lang", props.getLang())   // 言語（ja=日本語）
                .toUriString();

        try {
            // APIにリクエストを送ってJSONをWeatherResponseに変換して受け取る
            WeatherResponse response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(WeatherResponse.class);

            // データが空だった場合はエラーを投げる
            if (response == null || response.getWeather() == null || response.getWeather().isEmpty()) {
                throw new RuntimeException("天気データを取得できませんでした。");
            }

            // アイコンのURLを組み立てる（例：https://openweathermap.org/img/wn/01d@2x.png）
            String iconCode = response.getWeather().get(0).getIcon();
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

            // WeatherResponseから必要なデータだけ取り出してWeatherDataに詰め替えて返す
            return new WeatherData(
                    response.getName(),                              // 都市名
                    response.getMain().getTemp(),                    // 気温
                    response.getWeather().get(0).getDescription(),   // 天気の説明
                    response.getMain().getHumidity(),                // 湿度
                    response.getWind().getSpeed(),                   // 風速
                    iconUrl                                          // アイコンURL
            );

        } catch (HttpClientErrorException.NotFound e) {
            // 都市名が見つからなかった場合（404エラー）
            throw new RuntimeException("都市「" + city + "」が見つかりませんでした。スペルを確認してください。");
        } catch (HttpClientErrorException.Unauthorized e) {
            // APIキーが無効な場合（401エラー）
            throw new RuntimeException("APIキーが無効です。application.properties を確認してください。");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // その他のエラー
            throw new RuntimeException("天気情報の取得に失敗しました: " + e.getMessage(), e);
        }
    }
}
