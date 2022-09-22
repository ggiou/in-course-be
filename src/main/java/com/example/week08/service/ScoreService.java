package com.example.week08.service;

import com.example.week08.domain.Post;
import com.example.week08.dto.request.ScoreRequestDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final PostRepository postRepository;

    // 코스 게시글 평가 점수 주기
    @Transactional
    public void scoreCreate(Long courseId, ScoreRequestDto requestDto) throws IOException {

        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        post.createScore(requestDto);
        postRepository.save(post);
    }

}
