package com.example.week08.dto.response;

import com.example.week08.domain.Member;
import com.example.week08.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private boolean ok;
    private String message;
    private String Authorization;
    private String RefreshToken;
    private String email;
    private String nickname;
    private int emailAuth;

    public MemberResponseDto(boolean ok, String message, Member member) {
        this.ok = ok;
        this.message =message;
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }

    public MemberResponseDto(Member member) {
        this.ok = false;
        this.message = "이메일 인증이 필요합니다.";
        this.email = member.getEmail();
        this.emailAuth = member.getEmailAuth();
        this.nickname = member.getNickname();
    }

    public MemberResponseDto(TokenDto tokenDto, Member member) {
        this.ok = true;
        this.message ="로그인에 성공하였습니다.";
        this.Authorization ="Bearer "+ tokenDto.getAccessToken();
        this.RefreshToken = tokenDto.getRefreshToken();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.emailAuth = member.getEmailAuth();
    }
}
