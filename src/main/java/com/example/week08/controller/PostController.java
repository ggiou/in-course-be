package com.example.week08.controller;

import com.example.week08.domain.Post;
import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.dto.request.PlaceDeleteDto;
import com.example.week08.dto.request.PostPlaceDto;
import com.example.week08.dto.request.PostPlacePutDto;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.dto.response.PostResponseGetDto;
import com.example.week08.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

// 코스 게시글 작성(카드 이미지 통합)
    @PostMapping( "/api/course")
    public Post createPost(@RequestPart(value = "data") @Valid PostPlaceDto requestDto,
                           @RequestPart(value = "image" ,required = false) @Valid List<MultipartFile> image,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return postService.postCreate(requestDto, image, userDetails.getMember());
    }

    // 코스(게시글) 상세 조회
    @GetMapping( "/api/course/{courseId}")
    public PostResponseDto getPost(@PathVariable Long courseId) {
        return postService.getPost(courseId);
    }

//    // 코스(게시글) 전체 조회
//    @GetMapping("/api/course")
//    public List<PostResponseDto> getAllPosts() {
//        return postService.getAllPost();
//    }

    // 코스(게시글) 전체 조회
    @GetMapping("/api/course")
    public Page<Post> getAllPosts(Model model,
                                  @PageableDefault(size=15, sort="id",
            direction = Sort.Direction.DESC)Pageable pageable) {
        return postService.getAllPost(model,pageable);
    }

    // 코스(게시글) 수정
    @PutMapping( "/api/course/{courseId}")
    public Post updatePost(@PathVariable Long courseId,
                           @RequestPart(value = "data") @Valid PostPlacePutDto requestDto,
                           @RequestPart(value = "image" ,required = false) @Valid List<MultipartFile> image,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return postService.postUpdate(courseId, requestDto, image, userDetails.getMember());
    }

    // 코스(게시글) 삭제
    @DeleteMapping( "/api/course/{courseId}")
    public ResponseEntity<String> deletePost(@PathVariable Long courseId,
                                             @RequestPart(value = "data") @Valid PlaceDeleteDto requestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        postService.postDelete(courseId, requestDto, userDetails.getMember());
        return ResponseEntity.ok("게시물 삭제 성공");
    }

    // 코스(게시글) 카테고리 조회
    @GetMapping("/api/course/category")
    public List<PostResponseDto> findAll(@RequestBody(required = false) @Valid PostRequestDto requestDto){
        return postService.findPost(requestDto);
    }

    //메인 날씨/지역/계절/평점 기반 추천 회원용
    @GetMapping("/api/course/member/recommended")
    public Optional<PostResponseGetDto> recommendedGet(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.getRecommended(userDetails.getMember());
    }

    //메인 평점 기반 추천 비회원용
    @GetMapping("/api/course/common/recommended")
    public Optional<PostResponseGetDto>  commonRecommendedGet(){
        return postService.getCommonRecommended();
    }

    // 코스(게시글) 검색(제목, 내용, 카테고리 검색)
    @GetMapping("/api/course/search")
    public List<PostResponseDto> search(@RequestParam(value = "keyword") String keyword){
        return postService.searchPost(keyword);
    }

}
