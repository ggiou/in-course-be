package com.example.week08.domain;

import lombok.*;

import javax.persistence.*;
import com.amazonaws.services.ec2.model.EventType;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.request.ScoreRequestDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.hibernate.annotations.ColumnDefault;
import javax.validation.constraints.Min;
import java.util.ArrayList;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 500)
    private String image;

   @JoinColumn(name = "Member_id", nullable = false)
   @ManyToOne(fetch = FetchType.EAGER)
   private Member member;

    private String weather;
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

    @Column(nullable = false)
    private String region;
    @Getter
    public enum Region {
        CAPITAL("수도권"), GANGWON("강원"), CHUNGBUK("충북"), CHUNGNAM("충남"), JEONBUK("전북"), JEONNAM("전남"), GYEONGBUK("경북"), GYEONGNAM("경남"), JEJU("제주");
        public final String region;

        Region(String region) {
            this.region = region;
        }
    }

    @Column(nullable = false)
    private String season;
    @Getter
    public enum Season {
        SPRING("봄"), SUMMER("여름"), AUTUMN("가을"), WINTER("겨울");
        public final String season;

        Season(String season) {
            this.season = season;
        }
    }

    @Column(nullable = false)
    private String who;
    @Getter
    public enum Who {
        SOLO("혼자"), FAMILY("가족"), FRIEND("친구"), COUPLE("연인"), COLLEAGUE("동료");
        public final String who;

        Who(String who) {
            this.who = who;
        }
    }

    @ColumnDefault("0")
    private int num;

    @ColumnDefault("0")
    private int score;

    @ColumnDefault("0")
    private int avgScore;

    @Column
    private boolean newPost = true;

    //    @Column
//    private int avgScore;
//
//    @Getter
//    public enum Point {
//        Bad("20점"), NORMAL("40점"), GOOD("60점"), VERYGOOD("80점"), EXCELLENT("100점");
//        public final String point;
//
//        Point(String point) {
//            this.point = point;
//        }
//        @JsonCreator
//        public static EventType from(String s) {
//            return EventType.valueOf(s.toUpperCase());
//        }
//    }

    @ColumnDefault("0")
    @Min(0)
    private int heart;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL
//            , orphanRemoval = true
    )
    @JoinColumn(name = "Course_Id")
    private List<Place> place;

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

    // 코스(게시글) 찜하기
    public void addHeart() {
        this.heart += 1;
    }

    // 코스(게시글) 찜하기 취소
    public void deleteHeart() {
        this.heart -= 1;
    }

    // 코스(게시글) 평가 점수 주기
    public void createScore(ScoreRequestDto requestDto) {
        this.score += requestDto.getScore();
        this.num += 1;
        this.avgScore = (score/num);
    }
    public void setPlace(Place place) {
        this.place.add(place);
        place.setPost(this);
    }

}

