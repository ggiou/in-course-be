package com.example.week08.dto.response;

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
    private Long postId;
    private String postTitle;
    private Long memberId;
    private String email;
    private String nickname;

    public CourseHeartResponseDto(Post post, Member member) {
        this.message = "찜하기 성공";
        this.postId = post.getId();
        this.postTitle = post.getTitle();
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }

}
