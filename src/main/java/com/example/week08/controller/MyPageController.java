package com.example.week08.controller;

import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.dto.request.ProfileRequestDto;
import com.example.week08.dto.response.*;
import com.example.week08.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;

    @RequestMapping(value = "/api/member/mypage", method = RequestMethod.GET)
    public ProfileResponseDto getProfile(HttpServletRequest request){
        return myPageService.getProfile(request);
    }

    // 마이페이지 내가 작성한 코스(게시글) 조회
    @RequestMapping(value = "/api/member/mypage/post", method = RequestMethod.GET)
    public List<PostResponseDto> getPost(HttpServletRequest request){
        return myPageService.getPost(request);
    }


    // 마이페이지 내가 찜한 코스(게시글) 조회
    @RequestMapping(value = "/api/member/mypage/heart", method = RequestMethod.GET)
    public List<CourseHeartResponseDto> getHeart(HttpServletRequest request){
        return myPageService.getHeart(request);
    }

    @RequestMapping(value = "/api/member/mypage", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    public ProfileResponseDto updateProfile(@ModelAttribute ProfileRequestDto profileRequestDto, HttpServletRequest request) throws IOException {
        return myPageService.updateProfile(profileRequestDto, request);
    }

}
