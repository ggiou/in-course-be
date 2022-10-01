package com.example.week08.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRecommendedDto {
    //현재 날씨, 현재 지역, 현재 계절을 받음
    private String weather;
    private String region;
    private String season;

}
