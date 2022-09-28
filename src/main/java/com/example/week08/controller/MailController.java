package com.example.week08.controller;

import com.example.week08.dto.request.MailRequestDto;
import com.example.week08.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MailController {
    private final MailService emailService;

    @RequestMapping(value = "/api/member/signup_send", method = RequestMethod.POST)
    public String sendSignupMail(@RequestBody @Valid MailRequestDto mailRequestDto) throws Exception {
        emailService.sendSignupMail(mailRequestDto);
        return mailRequestDto.getEmail()+"로 회원가입 이메일 인증 번호가 성공적으로 전송됬습니다.";
    }

    @RequestMapping(value ="/api/member/signup_confirm", method = RequestMethod.POST)
    public String confirmSignupMail(@RequestBody @Valid MailRequestDto mailRequestDto) throws  Exception{
        emailService.confirmSignupMail(mailRequestDto);
        return mailRequestDto.getEmail()+"의 회원가입 이메일 인증이 성공했습니다.";
    }

}
