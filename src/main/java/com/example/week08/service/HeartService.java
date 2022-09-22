package com.example.week08.service;

import com.example.week08.domain.Post;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class HeartService {

    private final PostRepository postRepository;

    // 코스(게시글) 찜하기
    @Transactional
    public void addPostHeart(Long courseId) {

        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        post.addHeart();
        postRepository.save(post);
    }
    // 코스(게시글)찜하기 취소
    public void deletePostHeart(Long courseId) {

        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다..", ErrorCode.POST_NOT_EXIST)
        );
        post.deleteHeart();
        postRepository.save(post);
    }

}