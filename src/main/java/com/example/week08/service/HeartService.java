package com.example.week08.service;

import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.PlaceRepository;
import com.example.week08.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class HeartService {

    private final PostRepository postRepository;
    private final PlaceRepository placeRepository;

    // 코스(게시글) 찜하기
    @Transactional
    public void addPostHeart(Long courseId) {

        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        post.addHeart();
        postRepository.save(post);
    }
    // 코스(게시글) 찜하기 취소
    public void deletePostHeart(Long courseId) {

        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        post.deleteHeart();
        postRepository.save(post);
    }

    // 장소(카드) 찜하기
    @Transactional
    public void addPlaceHeart(Long placeId) {

        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new BusinessException("존재하지 않는 place id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        place.addHeart();
        placeRepository.save(place);
    }
    // 장소(카드) 찜하기 취소
    public void deletePlaceHeart(Long placeId) {

        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new BusinessException("존재하지 않는 place id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        place.deleteHeart();
        placeRepository.save(place);
    }

}