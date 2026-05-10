package com.weather.controller;

import com.weather.model.WeatherData;
import com.weather.service.WeatherService;
import org.springframework.stereotype.Controller; // Springが管理するコントローラクラスとして登録
import org.springframework.ui.Model; // HTMLにデータを渡す入れ物
import org.springframework.web.bind.annotation.GetMapping; // GETリクエストを受け取るアノテーション
import org.springframework.web.bind.annotation.RequestParam; // URLのパラメータを受け取るアノテーション

// ブラウザからのリクエストを受け取り、サービスと画面をつなぐクラス
@Controller
public class WeatherController {

    private final WeatherService weatherService; // 天気データを取得するサービス

    // コンストラクタ：WeatherServiceを受け取って使えるようにする
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // ブラウザが「/」にアクセスしてきたときに呼ばれるメソッド
    // 例：http://localhost:8081/?city=Tokyo
    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String city, // URLの「?city=Tokyo」の部分を受け取る（なくてもOK）
            Model model) {                               // HTMLにデータを渡す入れ物

        // cityが入力されている場合だけAPIを呼び出す
        if (city != null && !city.isBlank()) {
            try {
                // WeatherServiceに天気データを取得してもらう
                WeatherData weatherData = weatherService.getWeather(city);

                // 取得したデータをHTMLに渡す
                model.addAttribute("weather", weatherData);       // 天気データ
                model.addAttribute("searchedCity", city);         // 検索した都市名（検索ボックスに残す）
            } catch (RuntimeException e) {
                // エラーが起きた場合はエラーメッセージをHTMLに渡す
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("searchedCity", city);
            }
        }

        return "index"; // src/main/resources/templates/index.html を表示する
    }
}
