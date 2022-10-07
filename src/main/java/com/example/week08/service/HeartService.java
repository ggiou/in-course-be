package com.example.week08.service;

import com.example.week08.domain.Heart;
import com.example.week08.domain.Member;
import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.jwt.TokenProvider;
import com.example.week08.repository.HeartRepository;
import com.example.week08.repository.PlaceRepository;
import com.example.week08.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final PlaceRepository placeRepository;
    private final TokenProvider tokenProvider;

    // 코스(게시글) 찜하기
    @Transactional
    public void addPostHeart(Long courseId, HttpServletRequest request) {
        Member member = validateMember(request); //현재 로그인 중인 멤버
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        List<Heart> temp = heartRepository.findAllByPostId(courseId);

        if (!temp.isEmpty()) {
            for (Heart heart : temp) {
                boolean likeEquals = Objects.equals(heart.getMember().getEmail(), member.getEmail());
                if (likeEquals) {
                    throw new BusinessException(ErrorCode.FAIL_HEART);
                }
            }
        } //중복 like 배제
        post.addHeart();
        postRepository.save(post);

        Heart heart = Heart.builder()
                .postId(courseId)
                .email(member.getEmail())
                .member(member)
                .build();
        heartRepository.save(heart);
    }

    @Transactional
    // 코스(게시글) 찜하기 취소
    public void deletePostHeart(Long courseId, HttpServletRequest request) {
        Member member = validateMember(request); //현재 로그인 중인 멤버
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        Optional<Heart> temp = heartRepository.findByEmailAndPostId(member.getEmail(), courseId);
        if (temp.isEmpty()) {
            throw new BusinessException(ErrorCode.FAIL_DISHEART);
        }
        Heart heart = temp.get();
        if(!Objects.equals(heart.getMember().getId(), member.getId())){
            throw new BusinessException("해당 찜하기의 작성자가 아닙니다.",ErrorCode.FAIL_DISHEART);
        }
        if(!Objects.equals(heart.getPostId(), post.getId())){
            throw new BusinessException("해당 게시글의 찜하기 항목이 아닙니다.", ErrorCode.FAIL_DISHEART);
        } //해당 로그인 한 유저가 해당 게시글의 관심항목 작성자가 아닐 경우에는 예외처리 해줘야 함

        post.deleteHeart();
        postRepository.save(post);
        heartRepository.delete(heart);
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
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}