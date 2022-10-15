package com.example.week08.controller;

import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.dto.response.CourseHeartResponseDto;
import com.example.week08.service.CourseHeartService;
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
public class CourseHeartController {

    private final CourseHeartService courseHeartService;

    //코스(게시글) 찜하기
    @GetMapping( "/api/course/heart/{courseId}")
    public CourseHeartResponseDto addPostHeart(@PathVariable Long courseId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return courseHeartService.addPostHeart(courseId, userDetails.getMember());
    }

    // 코스(게시글) 찜하기 취소
    @GetMapping( "/api/course/disheart/{courseId}")
    public ResponseEntity<String> deletePostHeart(@PathVariable Long courseId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        courseHeartService.deletePostHeart(courseId, userDetails.getMember());
        return new ResponseEntity<>("찜하기 취소 성공", HttpStatus.OK);
    }
}
