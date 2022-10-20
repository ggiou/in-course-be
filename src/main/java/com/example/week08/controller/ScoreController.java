package com.example.week08.controller;

import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.dto.request.ScoreRequestDto;
import com.example.week08.dto.response.ScoreResponseDto;
import com.example.week08.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ScoreController {
    private final ScoreService scoreService;

    // 코스(게시글) 평가 점수 주기
    @PostMapping( "/api/course/score/{courseId}")
    public ResponseEntity<String> createScore(@PathVariable Long courseId,
                                              @RequestBody ScoreRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{
        scoreService.scoreCreate(courseId, requestDto, userDetails.getMember());
        return new ResponseEntity<>("평가 성공", HttpStatus.OK);
    }

    // 코스(게시글) 내가 준 평가 점수 조회
    @GetMapping( "/api/course/score/{courseId}")
    public ScoreResponseDto getScore(@PathVariable Long courseId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scoreService.getScore(courseId, userDetails.getMember());
    }


}
