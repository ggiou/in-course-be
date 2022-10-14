package com.example.week08.controller;

import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.dto.response.CourseHeartResponseDto;
import com.example.week08.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class HeartController {
    private final HeartService heartService;

    //코스(게시글) 찜하기
    @GetMapping( "/api/course/heart/{courseId}")
    public CourseHeartResponseDto addPostHeart(@PathVariable Long courseId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return heartService.addPostHeart(courseId, userDetails.getMember());
    }

    // 코스(게시글) 찜하기 취소
    @GetMapping( "/api/course/disheart/{courseId}")
    public ResponseEntity<String> deletePostHeart(@PathVariable Long courseId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        heartService.deletePostHeart(courseId, userDetails.getMember());
        return new ResponseEntity<>("찜하기 취소 성공", HttpStatus.OK);
    }

    // 장소(카드) 찜하기
    @GetMapping( "/api/course/place/heart/{placeId}")
    public ResponseEntity<String> addPlaceHeart(@PathVariable Long placeId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        heartService.addPlaceHeart(placeId, userDetails.getMember());
        return new ResponseEntity<>("찜하기 성공", HttpStatus.OK);
    }

    // 장소(카드) 찜하기 취소
    @GetMapping( "/api/course/place/disheart/{placeId}")
    public ResponseEntity<String> deletePlaceHeart(@PathVariable Long placeId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        heartService.deletePlaceHeart(placeId, userDetails.getMember());
        return new ResponseEntity<>("찜하기 취소 성공", HttpStatus.OK);
    }
}

