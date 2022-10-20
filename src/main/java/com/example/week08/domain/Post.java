package com.example.week08.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import com.amazonaws.services.ec2.model.EventType;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.request.ScoreRequestDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.hibernate.annotations.ColumnDefault;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    @Size(min = 2, max = 500)
    private String image;
    @NotNull
    private String region;
    @NotNull
    private String weather;
    @NotNull
    private String season;
    @NotNull
    private String who;
    @ColumnDefault("0")
    private double avgScore;
    @Column
    private boolean newPost = true;
    @ColumnDefault("0")
    private int heart;


    @Getter
    public enum Weather {
        SUNNY("맑음"), CLOUDY("흐림"), SNOW("눈"), RAINY("비");
        public final String weather;

        Weather(String weather) {
            this.weather = weather;
        }
        @JsonCreator
        public static EventType from(String s) {
            return EventType.valueOf(s.toUpperCase());
        }
    }

    @Getter
    public enum Region {
        CAPITAL("수도권"), GANGWON("강원"), CHUNGBUK("충북"), CHUNGNAM("충남"), JEONBUK("전북"), JEONNAM("전남"), GYEONGBUK("경북"), GYEONGNAM("경남"), JEJU("제주");
        public final String region;

        Region(String region) {
            this.region = region;
        }
    }

    @Getter
    public enum Season {
        SPRING("봄"), SUMMER("여름"), AUTUMN("가을"), WINTER("겨울");
        public final String season;

        Season(String season) {
            this.season = season;
        }
    }

    @Getter
    public enum Who {
        SOLO("혼자"), FAMILY("가족"), FRIEND("친구"), COUPLE("연인"), COLLEAGUE("동료");
        public final String who;

        Who(String who) {
            this.who = who;
        }
    }

    @JoinColumn(name = "Member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "Course_Id")
    private List<Place> place;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Course_Id")
    @JsonIgnore
    private List<CourseHeart> courseHeart;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Course_Id")
    private List<Score> scores;

    // 코스(게시글) 작성
    public Post(PostRequestDto postRequestDto, String image, Member member) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.weather = postRequestDto.getWeather();
        this.region = postRequestDto.getRegion();
        this.season = postRequestDto.getSeason();
        this.who = postRequestDto.getWho();
        this.image = image;
        this.member = member;
    }

    // 코스(게시글) 수정
    public void update(PostRequestDto postRequestDto, String image, Member member) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.weather = postRequestDto.getWeather();
        this.region = postRequestDto.getRegion();
        this.season = postRequestDto.getSeason();
        this.who = postRequestDto.getWho();
        this.image = image;
        this.member = member;
    }

    public void updatePostByNewPost(boolean newPost) {
        this.newPost = newPost;
    }

    // 코스(게시글) 찜하기 총 개수 저장
    public void addCountHeart(int countHeart) {
        this.heart = countHeart;
    }

    // 코스(게시글) 평가 점수 저장
    public void addAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }
    public void setPlace(Place place) {
        this.place.add(place);
        place.setPost(this);
    }

}

