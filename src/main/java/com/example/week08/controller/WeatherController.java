package com.example.week08.controller;

import com.example.week08.dto.request.WeatherDataRequestDto;
import com.example.week08.dto.response.MemberResponseDto;
import com.example.week08.dto.response.WeatherDataResponseDto;
import com.example.week08.util.weather.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class WeatherController {
    private final OpenWeatherService openWeather;

    @GetMapping( "/api/weather/open")
    public WeatherDataResponseDto saveWeather(@RequestBody @Valid WeatherDataRequestDto requestDto, HttpServletRequest request) throws Exception {
        return openWeather.restApiGetWeather(requestDto, request);
    }
}
