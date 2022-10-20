package com.example.week08.domain;


import com.example.week08.dto.request.PlaceRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Place extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String content;
    @NotNull
    private String address;//주소
    @NotNull
    private String coordinateX;//좌표 x
    @NotNull
    private String coordinateY;//좌표 y
    @Column
    private String placeImage;//이미지 url사용할거
    @NotNull
    private String placeName;//장소명
    @ColumnDefault("0")
    @Min(0)
    private int heart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    @JsonBackReference
    private Post post;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    @JsonIgnore
    private List<PlaceHeart> placeHeart;
    public Place(Post post, PlaceRequestDto placeRequestDto, String image) {
        this.content = placeRequestDto.getContent();
        this.address = placeRequestDto.getAddress();
        this.coordinateX = placeRequestDto.getCoordinateX();
        this.coordinateY = placeRequestDto.getCoordinateY();
        this.placeName = placeRequestDto.getPlaceName();
        this.placeImage = image;
        this.post = post;
    }
//    public Place(Post post, PlacePutDto placePutDto, String image) {
//        this.content = placePutDto.getContent();
//        this.address = placePutDto.getAddress();
//        this.coordinateX = placePutDto.getCoordinateX();
//        this.coordinateY = placePutDto.getCoordinateY();
//        this.placeName = placePutDto.getPlaceName();
//        this.placeImage = image;
//        this.post = post;
//    }

    public void update(PlaceRequestDto placeRequestDto, Post post, String image) {
        this.content = placeRequestDto.getContent();
        this.address = placeRequestDto.getAddress();
        this.coordinateX = placeRequestDto.getCoordinateX();
        this.coordinateY = placeRequestDto.getCoordinateY();
        this.placeName = placeRequestDto.getPlaceName();
        this.placeImage = image;
        this.post = post;
    }

    // 장소(카드) 찜하기 총 개수 저장
    public void addCountHeart(int countHeart) {
        this.heart = countHeart;
    }

}
