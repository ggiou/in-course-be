package com.example.week08.dto.response;

import com.example.week08.domain.CourseHeart;
import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseHeartResponseDto {
    private String message;
    private Post post;
    private Long memberId;
    private String email;
    private String nickname;

    public CourseHeartResponseDto(Post post, Member member) {
        this.message = "찜하기 성공";
        this.post = post;
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }

    public CourseHeartResponseDto(CourseHeart courseHeart) {
        this.message = "내가 찜한 course";
        this.post = courseHeart.getPost();
        this.memberId = courseHeart.getMember().getId();
        this.email = courseHeart.getMember().getEmail();
        this.nickname = courseHeart.getMember().getNickname();
    }

}
