package com.example.week08.dto.response;

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
    private String weather;
    private String region;
    private String season;
    private String who;
    private double avgScore;
    private int heart;
    private Long memberId;
    private String nickname;
    private String profileImage;
    private String location;
    private String badge;
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
        this.memberId = post.getMember().getId();
        this.nickname = post.getMember().getNickname();
        this.profileImage = post.getMember().getProfileImage();
        this.location = post.getMember().getLocation();
        this.badge = post.getMember().getBadge();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }


}
