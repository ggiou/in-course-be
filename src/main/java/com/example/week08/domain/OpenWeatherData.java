package com.example.week08.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OpenWeatherData extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    private String region;
    @NotNull
    private String weather; //날씨(맑음, 흐림, 눈, 비)
    @NotNull
    private String season; //계절
    @NotNull
    private String description; //날씨 설명
    @NotNull
    private double temp ; //온도
    @NotNull
    private int humidity ; //습도
    @NotNull
    private double wind_speed ; //풍속
    @NotNull
    private int clouds; //흐림 정도(%)
    @Column
    private int rain_h; //1시간 동안 강우량
    @Column
    private int snow_h; //1시간 동안 강우량

    public void update(String region, String weather, String season, String description, double temp, int humidity, double wind_speed, int clouds, int rain_h, int snow_h){
        this.region = region;
        this.weather = weather;
        this.season = season;
        this.description = description;
        this.temp = temp;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.clouds = clouds;
        this.rain_h = rain_h;
        this.snow_h = snow_h;
    }
}
