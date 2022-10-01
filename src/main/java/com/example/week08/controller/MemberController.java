package com.example.week08.controller;

import com.example.week08.dto.request.*;
import com.example.week08.dto.response.MemberResponseDto;
import com.example.week08.service.KakaoMemberService;
import com.example.week08.service.MemberService;

import com.example.week08.service.NaverLoginApi;
import com.example.week08.service.NaverMemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final KakaoMemberService kakaoMemberService;
    private final NaverMemberService naverMemberService;
    private final NaverLoginApi naverLoginApi;
    @RequestMapping(value = "/api/member/signup", method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberRequestDto requestDto){
        return memberService.createMember(requestDto);
    }
    @RequestMapping(value = "/api/member/signup/detail", method = RequestMethod.PUT)
    public ResponseEntity<MemberResponseDto> signupDetail(@RequestBody @Valid MemberDetailRequestDto requestDto){
        return memberService.detailMember(requestDto);
    }

    @RequestMapping(value = "/api/member/login", method = RequestMethod.POST)
    public ResponseEntity<MemberResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        return memberService.login(requestDto, response);
    }

    @RequestMapping(value = "/api/member/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(HttpServletRequest request){
        return memberService.logout(request);
    }

    @RequestMapping(value = "/api/member/kakao", method = RequestMethod.GET)
    public  ResponseEntity<KakaoMemberInfoDto> kakoLogin(@RequestParam String code, HttpServletResponse response)throws JsonProcessingException {
        log.info(code+"\n\n");
        return kakaoMemberService.kakakoLogin(code, response);
    }  // oauth2 카카오 로그인

    @RequestMapping(value = "/api/member/naver", method = RequestMethod.GET)
    public ResponseEntity<NaverMemberInfoDto> NaverLogin(@RequestParam String code, HttpServletResponse response)throws JsonProcessingException {
        log.info(code+"\n\n");
        return naverMemberService.naverLogin(code, response);
    }

//    @RequestMapping(value = "/api/member/naver", method = RequestMethod.GET)
//    public NaverMemberInfoDto NaverLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response)throws JsonProcessingException {
//        log.info(code);
//        return naverLoginApi.login(code,state,response);
//    } //네이버 로그인 API 형식

    @RequestMapping(value = "/api/member/naverUrl")
    public String naverLogin(){
        return "redirect:"+naverLoginApi.makeLoginUrl();
        //성공시 developer 지정한 call-back URL로 redirect
    }

    @ResponseBody
    @GetMapping("/api/member/kakao/code")
    public void kakaoCallback(@RequestParam String code) {
        System.out.println(code);
    }
}
