package com.example.week08.controller;

import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.service.PlaceHeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class PlaceHeartController {

    private final PlaceHeartService placeHeartService;

    // 장소(카드) 찜하기
    @GetMapping( "/api/course/place/heart/{placeId}")
    public ResponseEntity<String> addPlaceHeart(@PathVariable Long placeId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        placeHeartService.addPlaceHeart(placeId, userDetails.getMember());
        return new ResponseEntity<>("찜하기 성공", HttpStatus.OK);
    }

    // 장소(카드) 찜하기 취소
    @GetMapping( "/api/course/place/disheart/{placeId}")
    public ResponseEntity<String> deletePlaceHeart(@PathVariable Long placeId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        placeHeartService.deletePlaceHeart(placeId, userDetails.getMember());
        return new ResponseEntity<>("찜하기 취소 성공", HttpStatus.OK);
    }
    // 찜하기 체크
    @GetMapping("/api/course/place/heart/check/{placeId}")
    public boolean getplaceheart(@PathVariable Long placeId,
                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return placeHeartService.heartplaceget(placeId, userDetails.getMember());
    }

}
