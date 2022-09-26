package com.example.week08.dto.request;

import com.example.week08.domain.Member;
import com.example.week08.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor // 필드에 쓴 모든 생성자만 만들어줌
public class NaverMemberInfoDto {
    private boolean ok;
    private String message;
    private String Authorization;
    private String RefreshToken;
    private String naverId;
    private String nickname;
    private String email;
    private String image;

    public NaverMemberInfoDto(TokenDto tokenDto, Member member) {
        this.ok = true;
        this.message = member.getNickname()+"의 네이버 로그인에 성공하였습니다.";
        this.Authorization =tokenDto.getAccessToken();
        this.RefreshToken = tokenDto.getRefreshToken();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.naverId = member.getNaverId();
        this.image = member.getProfileImage();
    }

    public NaverMemberInfoDto(String email, String nickname, String naverId, String image) {
        this.email = email;
        this.nickname = nickname;
        this.naverId = naverId;
        this.image = image;
    }
}
