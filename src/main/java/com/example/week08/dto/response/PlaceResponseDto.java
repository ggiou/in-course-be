package com.example.week08.dto.response;

import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {
    private Long id;
    private String content;
    private String image;
    private String address;
    private String coordinateX;
    private String coordinateY;
    private String placeName;
    private int heartPlace;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PlaceResponseDto(Place place) {
        this.id = place.getId();
        this.content = place.getContent();
        this.image = place.getPlaceImage();
        this.address = place.getAddress();
        this.coordinateX = place.getCoordinateX();
        this.coordinateY = place.getCoordinateY();
        this.placeName = place.getPlaceName();
        this.heartPlace = place.getHeart_place();
//        this.post = place.getPost();
        this.createdAt = place.getCreatedAt();
        this.modifiedAt = place.getModifiedAt();
    }
}
