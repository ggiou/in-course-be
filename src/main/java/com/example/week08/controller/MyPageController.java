package com.example.week08.controller;

import com.example.week08.dto.request.ProfileRequestDto;
import com.example.week08.dto.response.MyHeartListDto;
import com.example.week08.dto.response.MyPostListResponseDto;
import com.example.week08.dto.response.ProfileResponseDto;
import com.example.week08.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;

    @RequestMapping(value = "/api/member/mypage", method = RequestMethod.GET)
    public ProfileResponseDto getProfile(HttpServletRequest request){
        return myPageService.getProfile(request);
    }

    @RequestMapping(value = "/api/member/mypage/post", method = RequestMethod.GET)
    public MyPostListResponseDto getPost(HttpServletRequest request){
        return myPageService.getPost(request);
    }

    @RequestMapping(value = "/api/member/mypage/heart", method = RequestMethod.GET)
    public MyHeartListDto getHeart(HttpServletRequest request){
        return myPageService.getHeart(request);
    }

    @RequestMapping(value = "/api/member/mypage", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    public ProfileResponseDto updateProfile(@ModelAttribute ProfileRequestDto profileRequestDto, HttpServletRequest request) throws IOException {
        return myPageService.updateProfile(profileRequestDto, request);
    }

}
