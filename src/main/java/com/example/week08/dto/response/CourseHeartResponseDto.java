package com.example.week08.dto.response;

import com.example.week08.domain.CourseHeart;
import com.example.week08.domain.Member;
import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseHeartResponseDto {
    private String message;
    private boolean heart;
    private Long memberId;
    private String myNickname;
    private Long courseId;
    private String title;
    private String writerNickname;
    private double avgScore;
    private int sumHeart;

//    private Post post;

    public CourseHeartResponseDto(Member member) {
        this.message = "찜하기 성공";
        this.heart = true;
        this.memberId = member.getId();
        this.myNickname = member.getNickname();
    }

    public CourseHeartResponseDto(CourseHeart courseHeart) {
        this.message = "내가 찜한 course";
        this.heart = true;
        this.myNickname = courseHeart.getMember().getNickname();
        this.memberId = courseHeart.getMember().getId();
        this.courseId = courseHeart.getPost().getId();
        this.title = courseHeart.getPost().getTitle();
        this.writerNickname = courseHeart.getPost().getMember().getNickname();
        this.avgScore = courseHeart.getPost().getAvgScore();
        this.sumHeart = courseHeart.getPost().getHeart();
    }

}
