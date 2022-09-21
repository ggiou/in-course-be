package com.example.week08.controller;

import com.example.week08.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class HeartController {
    private final HeartService heartService;

    // 코스(게시글) 찜하기
    @PutMapping( "/api/course/heart/{courseId}")
    public ResponseEntity<String> addPostHeart(@PathVariable Long courseId) {
        heartService.addPostHeart(courseId);
        return new ResponseEntity<>("찜하기 성공", HttpStatus.OK);
    }

    // 코스(게시글) 찜하기 취소
    @PutMapping( "/api/course/disheart/{courseId}")
    public ResponseEntity<String> deletePostHeart(@PathVariable Long courseId) {
        heartService.deletePostHeart(courseId);
        return new ResponseEntity<>("찜하기 취소 성공", HttpStatus.OK);
    }
}

