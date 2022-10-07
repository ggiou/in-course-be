package com.example.week08.util.weather;

import com.example.week08.domain.Member;
import com.example.week08.domain.OpenWeatherData;
import com.example.week08.domain.Post;
import com.example.week08.dto.request.WeatherDataRequestDto;
import com.example.week08.dto.response.WeatherDataResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.jwt.TokenProvider;
import com.example.week08.repository.OpenWeatherDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenWeatherService {
    private final TokenProvider tokenProvider;
    private final OpenWeatherDataRepository openWeatherDataRepository;

    @Transactional
    public WeatherDataResponseDto restApiGetWeather(WeatherDataRequestDto requestDto, HttpServletRequest request) throws Exception {
        Member member = validateMember(request);
        if (null == member) {
            throw new BusinessException("회원만 사용가능한 서비스 입니다.",ErrorCode.JWT_NOT_PERMIT);
        }

        String url = "https://api.openweathermap.org/data/2.5/weather?"//단기 예보 조회
                + "lat=" + requestDto.getY()//예보지점 y 좌표
                + "&lon=" + requestDto.getX()//예보지점 x 좌표
                + "&appid="//인증키
                + "&units=metric"//섭씨 온도
                + "&lang=kr";  //한국어

        System.out.println(url + "\n");
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);


            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
            System.out.println(uri.toUriString()+"\n");

            //이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

            //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
            ObjectMapper mapper = new ObjectMapper();


            String region = ((resultMap.getBody()).get("name").toString());
            Double temp = Double.valueOf(((LinkedHashMap) resultMap.getBody().get("main")).get("temp").toString());
            int humidity = Integer.valueOf(((LinkedHashMap) resultMap.getBody().get("main")).get("humidity").toString());
            Double wind = Double.valueOf(((LinkedHashMap) resultMap.getBody().get("wind")).get("speed").toString());
            int clouds = Integer.valueOf(((LinkedHashMap) resultMap.getBody().get("clouds")).get("all").toString());
            String description = ((LinkedHashMap) ((ArrayList) resultMap.getBody().get("weather")).get(0)).get("description").toString();
            String weather = ((LinkedHashMap) ((ArrayList) resultMap.getBody().get("weather")).get(0)).get("main").toString();
            if (weather.contains("Clear")) {
                weather = Post.Weather.SUNNY.getWeather();
            } else if (weather.contains("Snow")) {
                weather = Post.Weather.SNOW.getWeather();
            } else if (weather.contains("Rain") || weather.contains("Thunderstorm") || weather.contains("Drizzle")) {
                weather = Post.Weather.RAINY.getWeather();
            } else {
                weather = Post.Weather.CLOUDY.getWeather();
            }
            System.out.println(weather+"\n");

            int rain;
            int snow;
            if (weather.contains("비")) {
                rain = Integer.valueOf(((LinkedHashMap) resultMap.getBody().get("rain")).get("1h").toString());
            } else rain = 0;

            if (weather.contains("눈")) {
                snow = Integer.valueOf(((LinkedHashMap) resultMap.getBody().get("snow")).get("1h").toString());
            } else snow = 0;


            LocalDate now = LocalDate.now();
            int monthValue = now.getMonthValue();
            String season;
            if (monthValue >= 3 && monthValue <= 5) {
                season = Post.Season.SPRING.getSeason();
            } else if (monthValue >= 6 && monthValue <= 8) {
                season = Post.Season.SUMMER.getSeason();
            } else if (monthValue >= 9 && monthValue <= 11) {
                season = Post.Season.AUTUMN.getSeason();
            } else {
                season = Post.Season.WINTER.getSeason();
            }

            if (openWeatherDataRepository.findByMember(member).isEmpty()) { //처음 조회 시
                OpenWeatherData newData = OpenWeatherData.builder()
                        .member(member)
                        .region(region)
                        .season(season)
                        .weather(weather)
                        .description(description)
                        .temp(temp)
                        .clouds(clouds)
                        .humidity(humidity)
                        .snow_h(snow)
                        .rain_h(rain)
                        .wind_speed(wind)
                        .build();
                openWeatherDataRepository.save(newData);
            } else {
                OpenWeatherData dt = isPresent(member);
                dt.update(region, weather, season, description, temp, humidity, wind, clouds, rain, snow);
                openWeatherDataRepository.save(dt);
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
            System.out.println(e.toString());
        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body", "excpetion오류");
            System.out.println(e.toString());
        }
        Optional<OpenWeatherData> d = openWeatherDataRepository.findByMember(member);
        OpenWeatherData data = d.get();
        System.out.println(data.getMember().getNickname()+"\n");
        return new WeatherDataResponseDto(data);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public OpenWeatherData isPresent(Member member) {
        Optional<OpenWeatherData> optionalData = openWeatherDataRepository.findByMember(member);
        return optionalData.orElse(null);
    }
}
