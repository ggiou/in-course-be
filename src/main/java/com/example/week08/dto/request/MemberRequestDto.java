package com.example.week08.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    @NotBlank
    @Pattern(regexp = "^[?=.*!a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$", message = "이메일 형식으로 입력해 주십시오.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$", message = "비밀번호는 최소 8글자 이상 20자 이하며 하나의 대소문자와 숫자가 포함되야 합니다.")
    private String password;

    @NotBlank
    private String passwordConfirm;
}
