package com.weather;

import org.springframework.boot.SpringApplication; // アプリを起動するクラス
import org.springframework.boot.autoconfigure.SpringBootApplication; // Spring Bootアプリとして動かすアノテーション

// アプリの起動クラス（ここが一番最初に実行される）
@SpringBootApplication
public class WeatherAppApplication {

    public static void main(String[] args) {
        // Spring Bootアプリを起動する
        SpringApplication.run(WeatherAppApplication.class, args);
    }
}
