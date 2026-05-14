package com.weather.config;

import org.springframework.boot.context.properties.ConfigurationProperties; // application.propertiesの設定を読み込む
import org.springframework.stereotype.Component; // Springが管理するクラスとして登録

import lombok.Data; // getter・setterを自動生成

// application.propertiesの「weather.api.〇〇」をまとめて管理するクラス
@Data
@Component
@ConfigurationProperties(prefix = "weather.api") // "weather.api"から始まる設定を自動で読み込む
public class WeatherProperties {

    private String key;     // APIキー（weather.api.key）
    private String baseUrl; // APIのURL（weather.api.base-url）
    private String units;   // 単位設定（weather.api.units）例：metric=摂氏
    private String lang;    // 言語設定（weather.api.lang）例：ja=日本語
}
