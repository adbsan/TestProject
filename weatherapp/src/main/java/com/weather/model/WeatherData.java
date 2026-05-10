package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data //getter・setterを自動生成
@AllArgsConstructor //全変数に値を入れてオブジェクトを作る仕組みを自動生成
public class WeatherData {
	
	// 天気情報を画面に渡すための変数
    private String cityName;    // 都市名
    private double temperature; // 気温（℃）
    private String description; // 天気の説明（例：晴天）
    private int humidity;       // 湿度（%）
    private double windSpeed;   // 風速（m/s）
    private String iconUrl;     // 天気アイコンの画像URL
}
