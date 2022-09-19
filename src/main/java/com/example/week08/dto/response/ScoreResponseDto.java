package com.example.week08.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponseDto {
    private Long id;
    private double score;
    private double avgScore;
    private Long postId;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
