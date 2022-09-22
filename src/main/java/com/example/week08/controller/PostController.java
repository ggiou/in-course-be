package com.example.week08.controller;

import com.example.week08.domain.Post;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;


    // 코스 게시글 작성
    @PostMapping( "/api/course")
    public Post createPost(@RequestPart(value = "data") PostRequestDto requestDto,
                           @RequestPart(value = "image" ,required = false) MultipartFile image) throws IOException {
        return postService.postCreate(requestDto, image);
    }

    // 코스(게시글) 상세 조회
    @GetMapping( "/api/course/{courseId}")
    public PostResponseDto getPost(@PathVariable Long courseId) {
        return postService.getPost(courseId);
    }

    // 코스(게시글) 전체 조회
    @GetMapping("/api/course")
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPost();
    }

    // 코스(게시글) 수정
    @PutMapping( "/api/course/{courseId}")
    public Post updatePost(@PathVariable Long courseId,
                           @RequestPart(value = "data") PostRequestDto requestDto,
                           @RequestPart(value = "image" ,required = false) MultipartFile image) throws IOException {
        return postService.postUpdate(courseId, requestDto, image);
    }

    // 코스(게시글) 삭제
    @DeleteMapping( "/api/course/{courseId}")
    public ResponseEntity<String> deletePost(@PathVariable Long courseId) {
        postService.postDelete(courseId);
        return ResponseEntity.ok("게시물 삭제 성공");
    }


}
