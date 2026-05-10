package com.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // 知らない項目は無視していいよという設定
import lombok.Data; // getter・setterを自動生成
import java.util.List;

// APIから受け取ったJSONをそのまま格納する入れ物
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // APIの不要なデータを無視する
public class WeatherResponse {

    private String name;           // 都市名
    private Main main;             // 気温・湿度のデータ
    private Wind wind;             // 風速のデータ
    private List<Weather> weather; // 天気の説明・アイコンのデータ（リスト形式）

    // 気温・湿度を格納するクラス（JSONの"main"に対応）
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private double temp;  // 気温（℃）
        private int humidity; // 湿度（%）
    }

    // 風速を格納するクラス（JSONの"wind"に対応）
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        private double speed; // 風速（m/s）
    }

    // 天気の説明・アイコンを格納するクラス（JSONの"weather"に対応）
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String description; // 天気の説明（例：晴天）
        private String icon;        // 天気アイコンのコード（例：01d）
    }
}
