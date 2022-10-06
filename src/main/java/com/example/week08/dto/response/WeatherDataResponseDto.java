package com.example.week08.dto.response;


import com.example.week08.domain.OpenWeatherData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataResponseDto {
    private String member;
    private String region;
    private String weather; //날씨(맑음, 흐림, 눈, 비)
    private String season; //계절
    private String description; //날씨 설명
    private double temp ; //온도
    private int humidity ; //습도
    private double wind_speed ; //풍속
    private int clouds; //흐림 정도(%)
    private int rain_h; //1시간 동안 강우량
    private int snow_h; //1시간 동안 강우량

    public WeatherDataResponseDto(OpenWeatherData data){
        this.member = data.getMember().getNickname();
        this.region = data.getRegion();
        this.season = data.getSeason();
        this.weather = data.getWeather();
        this.description = data.getDescription();
        this.temp =data.getTemp();
        this.humidity = data.getHumidity();
        this.wind_speed = data.getWind_speed();
        this.clouds = data.getClouds();
        this.rain_h = data.getRain_h();
        this.snow_h = data.getSnow_h();
    }
}
