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
    private String location;
    private String profileImage;
    private String password;

    public ProfileResponseDto(Member member){
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.location = member.getLocation();
        this.profileImage = member.getProfileImage();
        this.password = member.getPassword();
    }
}
