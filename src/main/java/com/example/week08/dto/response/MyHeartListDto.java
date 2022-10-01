package com.example.week08.dto.response;

import com.example.week08.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class MyHeartListDto {

    private final List<Post> myHeartList = new ArrayList<>();

    public MyHeartListDto(List<Optional<Post>> heartList){
        for (Optional<Post> post : heartList){
            this.myHeartList.add(Post.builder()
                    .id(post.get().getId())
                    .title(post.get().getTitle())
                    .content(post.get().getContent())
                    .image(post.get().getImage())
                    .avgScore(post.get().getAvgScore())
                    .heart(post.get().getHeart())
                    .member(post.get().getMember())
                    .build());
        }
    }
}
