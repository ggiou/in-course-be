package com.example.week08.dto.response;

import com.example.week08.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String gender;
    private String location;
    private String image;
    private String password;

    private String naverId;
    private Long kakaoId;
    private String badge;
    private int heartSum;

    public ProfileResponseDto(Member member){
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.gender = member.getGender();
        this.location = member.getLocation();
        this.image = member.getProfileImage();
        this.password = member.getPassword();
        this.naverId = member.getNaverId();
        this.kakaoId = member.getKakaoId();
        this.badge = member.getBadge();
        this.heartSum = member.getHeartSum();
    }
}
