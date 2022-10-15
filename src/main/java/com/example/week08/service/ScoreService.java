package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import com.example.week08.domain.Score;
import com.example.week08.dto.request.ScoreRequestDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.PostRepository;
import com.example.week08.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final PostRepository postRepository;
    private final ScoreRepository scoreRepository;

    // 코스(게시글) 평가 점수 주기
    @Transactional
    public void scoreCreate(Long courseId, ScoreRequestDto scoreRequestDto, Member member) throws IOException {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );

        if (scoreRepository.findScoreByPostAndMember(post, member).isPresent()) {
            scoreRepository.deleteByPostAndMember(post, member);
        }

        scoreRepository.save(new Score(post, scoreRequestDto, member));

        double avgScore = scoreRepository.findAvgScore(courseId);

        post.addAvgScore(avgScore);
        postRepository.save(post);
    }

}
