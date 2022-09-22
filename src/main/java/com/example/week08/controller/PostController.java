package com.example.week08.controller;

import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.request.ScoreRequestDto;
import com.example.week08.dto.response.ResponseDto;
import com.example.week08.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;


    // 코스 게시글 작성
    @PostMapping( "/api/course")
    public ResponseDto<?> createPost(@RequestPart(value = "data") PostRequestDto requestDto,
                                     @RequestPart(value = "image" ,required = false) MultipartFile file,
                                     HttpServletRequest request) {
        return postService.createPost(requestDto, file, request);
    }

    // 코스 게시글 상세 조회
    @GetMapping( "/api/course/{courseId}")
    public ResponseDto<?> getPost(@PathVariable Long courseId) {
        return postService.getPost(courseId);
    }

    // 전체 코스 게시글 조회
    @GetMapping("/api/course")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    // 코스 게시글 수정
    @PutMapping( "/api/course/{courseId}")
    public ResponseDto<?> updatePost(@PathVariable Long courseId,
                                     @RequestPart(value = "data") PostRequestDto requestDto,
                                     @RequestPart(value = "image" ,required = false) MultipartFile file,
                                     HttpServletRequest request) {
        return postService.updatePost(courseId, requestDto, file, request);
    }

    // 코스 게시글 삭제
    @DeleteMapping( "/api/course/{courseId}")
    public ResponseDto<?> deletePost(@PathVariable Long courseId,
                                     HttpServletRequest request) {
        return postService.deletePost(courseId, request);
    }


}
