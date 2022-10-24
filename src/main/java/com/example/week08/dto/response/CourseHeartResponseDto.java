package com.example.week08.dto.response;

import com.example.week08.domain.CourseHeart;
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
public class CourseHeartResponseDto {
    private String message;
    private boolean heart;
    private Long memberId;
    private String myNickname;
    private Long postId;
    private String title;
    private String content;
    private String image;
    private String weather;
    private String region;
    private String season;
    private String who;
    private List<Place> place;
    private Long writerId;
    private String writerNickname;
    private String profileImage;
    private String location;
    private String badge;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private double avgScore;
    private int sumHeart;
//    private Post post;

    public CourseHeartResponseDto(Member member) {
        this.message = "찜하기 성공";
        this.heart = true;
        this.memberId = member.getId();
        this.myNickname = member.getNickname();
    }

    public CourseHeartResponseDto(CourseHeart courseHeart) {
        this.message = "내가 찜한 course";
        this.heart = true;
        this.memberId = courseHeart.getMember().getId();
        this.myNickname = courseHeart.getMember().getNickname();
        this.postId = courseHeart.getPost().getId();
        this.title = courseHeart.getPost().getTitle();
        this.content = courseHeart.getPost().getContent();
        this.image = courseHeart.getPost().getImage();
        this.weather = courseHeart.getPost().getWeather();
        this.region = courseHeart.getPost().getRegion();
        this.season = courseHeart.getPost().getSeason();
        this.who = courseHeart.getPost().getWho();
        this.avgScore = courseHeart.getPost().getAvgScore();
        this.sumHeart = courseHeart.getPost().getHeart();
        this.place = courseHeart.getPost().getPlace();
        this.writerId = courseHeart.getMember().getId();
        this.writerNickname = courseHeart.getPost().getMember().getNickname();
        this.profileImage = courseHeart.getMember().getProfileImage();
        this.location = courseHeart.getMember().getLocation();
        this.badge = courseHeart.getMember().getBadge();
        this.createdAt = courseHeart.getPost().getCreatedAt();
        this.modifiedAt = courseHeart.getPost().getModifiedAt();
    }

}
