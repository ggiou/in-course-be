package com.example.week08.dto.response;

import com.example.week08.domain.Score;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponseDto {
    private Long id;
    private String nickname;
    private double score;
    private Long courseId;
    private String title;
    private double avgScore;

    public ScoreResponseDto(Score score) {
        this.id = score.getMember().getId();
        this.nickname = score.getMember().getNickname();
        this.score = score.getScore();
        this.courseId = score.getPost().getId();
        this.title = score.getPost().getTitle();
        this.avgScore = score.getPost().getAvgScore();

    }
}
