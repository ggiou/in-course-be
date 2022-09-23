package com.example.week08.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Get메소드 일괄 생성
@NoArgsConstructor // 파라미터가 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 만듦
public class PostRequestDto {
    private String title;
    private String content;
    private String image;
    private String weather;
    private String region;
    private String season;
    private String who;


}

