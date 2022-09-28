package com.example.week08.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailRequestDto {
    @NotBlank
    private String email;

    @Size(min = 1, max = 12)
    private String nickname;

    private String location;
}
