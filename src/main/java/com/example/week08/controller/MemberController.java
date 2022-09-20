package com.example.week08.controller;

import com.example.week08.dto.request.LoginRequestDto;
import com.example.week08.dto.request.MailRequestDto;
import com.example.week08.dto.request.MemberRequestDto;
import com.example.week08.dto.response.MailResponseDto;
import com.example.week08.dto.response.MemberResponseDto;
import com.example.week08.service.MailService;
import com.example.week08.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @RequestMapping(value = "/api/member/signup", method = RequestMethod.POST)
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberRequestDto requestDto){
        memberService.createMember(requestDto);
        return ResponseEntity.ok(new MemberResponseDto(true, requestDto.getNickname()+"님의 회원가입에 성공하였습니다."));
    }

    @RequestMapping(value = "/api/member/login", method = RequestMethod.POST)
    public ResponseEntity<MemberResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        return memberService.login(requestDto, response);
    }

    @RequestMapping(value = "/api/member/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(HttpServletRequest request){
        return memberService.logout(request);
    }


}
