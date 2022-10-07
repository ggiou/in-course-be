package com.example.week08.domain;


import com.example.week08.dto.request.PlacePutDto;
import com.example.week08.dto.request.PlaceRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Min;

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

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String address;//주소

    @Column(nullable = false)
    private String coordinateX;//좌표 x

    @Column(nullable = false)
    private String coordinateY;//좌표 y

    @Column
    private String placeImage;//이미지 url사용할거
    @Column
    private String placeName;

    @ColumnDefault("0")
    @Min(0)
    private int heart_place;

//    @Column(nullable = false)
//    private Long Course_id;

//    @ManyToOne
//    @JoinColumn(name = "Member_id", nullable = false)
//    private Member member;

    @JoinColumn(name = "Course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Post post;

    public Place(Post post, PlaceRequestDto placeRequestDto, String image) {
        this.content = placeRequestDto.getContent();
        this.address = placeRequestDto.getAddress();
        this.coordinateX = placeRequestDto.getCoordinateX();
        this.coordinateY = placeRequestDto.getCoordinateY();
        this.placeName = placeRequestDto.getPlaceName();
        this.placeImage = image;
        this.post = post;
    }

    public void update(PlacePutDto placePutDto, Post post, String image) {
        this.content = placePutDto.getContent();
        this.address = placePutDto.getAddress();
        this.coordinateX = placePutDto.getCoordinateX();
        this.coordinateY = placePutDto.getCoordinateY();
        this.placeName = placePutDto.getPlaceName();
        this.placeImage = image;
        this.post = post;
    }

    // 장소(카드) 찜하기
    public void addHeart() {
        this.heart_place += 1;
    }

    // 장소(카드) 찜하기 취소
    public void deleteHeart() {
        this.heart_place -= 1;
    }

}
