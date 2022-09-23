package com.example.week08.domain;


import com.example.week08.dto.request.PlacePutDto;
import com.example.week08.dto.request.PlaceRequestDto;
import com.example.week08.repository.PlaceRepository;
import com.example.week08.repository.PostRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

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
    private String image;//이미지 url사용할거

    @Column(nullable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

    @JoinColumn(name = "CourseId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Post post;

    public Place(Long postId, PlaceRequestDto placeRequestDto
//            , Member member
    ) {
        this.content = placeRequestDto.getContent();
        this.address = placeRequestDto.getAddress();
        this.coordinateX = placeRequestDto.getCoordinateX();
        this.coordinateY = placeRequestDto.getCoordinateY();
        this.image = placeRequestDto.getImage();
        this.postId = postId;
//        this.member = member;
    }

    public void update(PlacePutDto placePutDto) {
        this.content = placePutDto.getContent();
        this.address = placePutDto.getAddress();
        this.coordinateX = placePutDto.getCoordinateX();
        this.coordinateY = placePutDto.getCoordinateY();
        this.image = placePutDto.getImage();
    }
}
