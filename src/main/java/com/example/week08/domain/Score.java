package com.example.week08.domain;

import com.example.week08.dto.request.ScoreRequestDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Post post;
    @ManyToOne
    @JoinColumn
    private Member member;

    @Column
    private int score;


    public Score(Post post, ScoreRequestDto scoreRequestDto, Member member) {
        this.post = post;
        this.score = scoreRequestDto.getScore();
        this.member = member;
    }
}
