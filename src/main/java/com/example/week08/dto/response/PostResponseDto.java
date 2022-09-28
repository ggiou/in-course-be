package com.example.week08.dto.response;

import com.example.week08.domain.Member;
import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
//    private String place1;
//    private String place2;
//    private String place3;
    private String weather;
    private String region;
    private String season;
    private String who;
    private int avgScore;
    private int heart;
    private String courseMap;
    private Member member;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Place> place;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getImage();
        this.weather = post.getWeather();
        this.region = post.getRegion();
        this.season = post.getSeason();
        this.who = post.getWho();
        this.avgScore = post.getAvgScore();
        this.heart = post.getHeart();
        this.place = post.getPlace();
//        this.member = post.getMember();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }


}
