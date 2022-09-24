package com.example.week08.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class PostPlacePutDto {
    private PostRequestDto postRequestDto;
    private ArrayList<PlacePutDto> placePutDtoList;

}