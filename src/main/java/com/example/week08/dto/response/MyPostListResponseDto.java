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
public class MyPostListResponseDto {

    private List<Post> myPostList = new ArrayList<>();

    public MyPostListResponseDto(List<Post> mypost){
        for (Post post: mypost){
            this.myPostList.add(Post.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .image(post.getImage())
//                            .place(post.getPlace)
                            .avgScore(post.getAvgScore())
                            .heart(post.getHeart())
                            .member(post.getMember())
                    .build());
        }

    }
}
