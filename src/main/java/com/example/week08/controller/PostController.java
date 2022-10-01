package com.example.week08.controller;

import com.example.week08.domain.Post;
import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.dto.request.PostPlaceDto;
import com.example.week08.dto.request.PostPlacePutDto;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    // 코스 게시글 작성
    @PostMapping( "/api/course")
    public Post createPost(@RequestPart(value = "data") PostPlaceDto requestDto,
                           @RequestPart(value = "image" ,required = false) MultipartFile image,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return postService.postCreate(requestDto, image, userDetails.getMember());
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
                           @RequestPart(value = "data") PostPlacePutDto requestDto,
                           @RequestPart(value = "image" ,required = false) MultipartFile image,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return postService.postUpdate(courseId, requestDto, image, userDetails.getMember());
    }

    // 코스(게시글) 삭제
    @DeleteMapping( "/api/course/{courseId}")
    public ResponseEntity<String> deletePost(@PathVariable Long courseId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnsupportedEncodingException {
        postService.postDelete(courseId, userDetails.getMember());
        return ResponseEntity.ok("게시물 삭제 성공");
    }

    // 코스(게시글) 카테고리 조회
    @GetMapping("/api/course/category")
    public List<PostResponseDto> findAll(@RequestBody(required = false) PostRequestDto requestDto){
//        if (requestDto == null) return postService.findAll();
        return postService.findPost(requestDto);
    }

    // 코스(게시글) 검색(제목, 내용, 카테고리 검색)
    @GetMapping("/api/course/search")
    public List<PostResponseDto> search(@RequestParam(value = "keyword") String keyword){
        return postService.searchPost(keyword);
    }

}
