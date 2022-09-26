package com.example.week08.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRequestDto {

    private String content;
    private String address;
    private String coordinateX;
    private String coordinateY;
    private String image;

}