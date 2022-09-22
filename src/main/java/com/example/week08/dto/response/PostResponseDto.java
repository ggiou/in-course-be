package com.example.week08.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String image;
    private String course1;// 메인(로그인 안했을 시)에서 추천코스 안에 코스1, 코스2 누르면 카드로 넘어가는거? 아님 뒤에 제목, 내용, 이미지, 카테고리, 태그 이런게 바뀌는거?? -> 19일 16시 회의 텍스트만 뜨는걸로
    private String course2;
//    private String course3;
    private String category;
    private String tag;
    private int score;
    private int heart;
    private String courseMap;
    private String member;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
