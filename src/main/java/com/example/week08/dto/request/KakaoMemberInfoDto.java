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
public class KakaoMemberInfoDto {

    private boolean ok;
    private String message;
    private String Authorization;
    private String RefreshToken;
    private Long kakaoId;
    private String nickname;
    private String email;
    private String image;
    public KakaoMemberInfoDto(TokenDto tokenDto, Member member) {
        this.ok = true;
        this.message = member.getNickname()+"의 카카오 로그인에 성공하였습니다.";
        this.Authorization ="Bearer "+tokenDto.getAccessToken();
        this.RefreshToken = tokenDto.getRefreshToken();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.kakaoId = member.getKakaoId();
        this.image = member.getProfileImage();
    }

    public KakaoMemberInfoDto(Long kakaoId,  String nickname, String email, String image) {
        this.email = email;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.image = image;
    }
}
