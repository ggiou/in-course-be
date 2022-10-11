package com.example.week08.dto.response;

import com.example.week08.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPostListResponseDto {

    private final List<PostResponseDto> myPostList = new ArrayList<>();

    public MyPostListResponseDto(List<Post> mypost) {
        for (Post post : mypost) {
            myPostList.add(PostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .image(post.getImage())
                    .weather(post.getWeather())
                    .region(post.getRegion())
                    .season(post.getSeason())
                    .who(post.getWho())
                    .avgScore(post.getAvgScore())
                    .heart(post.getHeart())
                    .place(post.getPlace())
                    .memberId(post.getMember().getId())
                    .email(post.getMember().getEmail())
                    .nickname(post.getMember().getNickname())
                    .profileImage(post.getMember().getProfileImage())
                    .location(post.getMember().getLocation())
                    .kakaoId(post.getMember().getKakaoId())
                    .naverId(post.getMember().getNaverId())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build());
        }
    }
}
