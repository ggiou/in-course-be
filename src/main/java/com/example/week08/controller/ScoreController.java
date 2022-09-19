package com.example.week08.controller;

import com.example.week08.dto.request.ScoreRequestDto;
import com.example.week08.dto.response.ResponseDto;
import com.example.week08.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ScoreController {
    private final ScoreService scoreService;

    // 코스 게시글 평가 점수 작성
    @PostMapping( "/api/course/score/{courseId}")
    public ResponseDto<?> createScore(@PathVariable Long courseId,
                                      @RequestBody ScoreRequestDto requestDto,
                                      HttpServletRequest request) {
        return scoreService.createScore(courseId, requestDto, request);
    }



}
