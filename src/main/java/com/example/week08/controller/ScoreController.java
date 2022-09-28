package com.example.week08.controller;

import com.example.week08.dto.request.ScoreRequestDto;
import com.example.week08.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
public class ScoreController {
    private final ScoreService scoreService;

    // 코스(게시글) 평가 점수 주기
    @PostMapping( "/api/course/score/{courseId}")
    public ResponseEntity<String> createScore(@PathVariable Long courseId,
                                              @RequestBody ScoreRequestDto requestDto) throws IOException{
        scoreService.scoreCreate(courseId, requestDto);
        return new ResponseEntity<>("평가 성공", HttpStatus.OK);
    }

}
