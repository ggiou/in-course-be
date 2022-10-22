package com.example.week08.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {
    private String nickname;

    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$", message = "비밀번호는 최소 8글자 이상 20자 이하며 하나의 대소문자와 숫자가 포함되야 합니다.")
    private String password;

    private String passwordConfirm;

    private String location;

    private MultipartFile image;

    private String gender;
}
