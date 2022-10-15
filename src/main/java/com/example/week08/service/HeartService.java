package com.example.week08.service;

import com.example.week08.domain.*;
import com.example.week08.dto.response.CourseHeartResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;

import com.example.week08.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HeartService {

    private final CourseHeartRepository courseHeartRepository;
    private final PlaceHeartRepository placeHeartRepository;
    private final PostRepository postRepository;
    private final PlaceRepository placeRepository;

    // 코스(게시글) 찜하기
    @Transactional
    public CourseHeartResponseDto addPostHeart(Long courseId, Member member) {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 course id 입니다.", ErrorCode.POST_NOT_EXIST)
        );

        if (courseHeartRepository.findByPostAndMember(post, member).isPresent()) {
            throw new BusinessException("이미 찜한 course 입니다.", ErrorCode.ALREADY_HEARTED);
        }

        courseHeartRepository.save(new CourseHeart(post, member));
        int countHeart = courseHeartRepository.findCountHeart(courseId);

        post.addCountHeart(countHeart);

        return new CourseHeartResponseDto(post, member);

    }

    // 코스(게시글) 찜하기 취소
    @Transactional
    public void deletePostHeart(Long courseId, Member member) {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 course id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        Optional<CourseHeart> heartOptional = courseHeartRepository.findByPostAndMember(post, member);

        if (heartOptional.isEmpty()) {
            throw new BusinessException("찜하지 않은 course 입니다.", ErrorCode.HEART_NOT_FOUND);
        }

        courseHeartRepository.delete(heartOptional.get());
        int countHeart = courseHeartRepository.findCountHeart(courseId);

        post.addCountHeart(countHeart);

    }

    // 장소(카드) 찜하기
    @Transactional
    public void addPlaceHeart(Long placeId, Member member) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new BusinessException("존재하지 않는 place id 입니다.", ErrorCode.PLACE_NOT_EXIST)
        );

        if (placeHeartRepository.findByPlaceAndMember(place, member).isPresent()) {
            throw new BusinessException("이미 찜한 place 입니다.", ErrorCode.ALREADY_HEARTED);
        }

        placeHeartRepository.save(new PlaceHeart(place, member));
        int countHeart = placeHeartRepository.findCountHeart(placeId);

        place.addCountHeart(countHeart);

    }

    // 장소(카드) 찜하기 취소
    @Transactional
    public void deletePlaceHeart(Long placeId, Member member) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new BusinessException("존재하지 않는 place id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        Optional<PlaceHeart> heartOptional = placeHeartRepository.findByPlaceAndMember(place, member);

        if (heartOptional.isEmpty()) {
            throw new BusinessException("찜하지 않은 course 입니다.", ErrorCode.HEART_NOT_FOUND);
        }

        placeHeartRepository.delete(heartOptional.get());

        int countHeart = placeHeartRepository.findCountHeart(placeId);

        place.addCountHeart(countHeart);
    }


}