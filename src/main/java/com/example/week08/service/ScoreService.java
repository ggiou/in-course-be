package com.example.week08.service;

import com.example.week08.domain.Post;
import com.example.week08.domain.Score;
import com.example.week08.dto.request.ScoreRequestDto;
import com.example.week08.dto.response.ResponseDto;
import com.example.week08.dto.response.ScoreResponseDto;
import com.example.week08.repository.PostRepository;
import com.example.week08.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final PostRepository postRepository;
    private final ScoreRepository scoreRepository;

    // 코스 게시글 평가 점수 작성
    @Transactional
    public ResponseDto<?> createScore(Long courseId,
                                      ScoreRequestDto requestDto,
                                      HttpServletRequest request
    ) {

//        if (null == request.getHeader("RefreshToken")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        Member member = validateMember(request);
//        if (null == member) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }

        // 코스 게시글 유무 검사
        Post post = isPresentPost(courseId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }


        Score score = Score.builder()
                .score(requestDto.getScore())
                .post(post)
//                .member(member)
                .build();

        scoreRepository.save(score);

        return ResponseDto.success(
                ScoreResponseDto.builder()
                        .id(score.getId())
                        .score(score.getScore())
                        .postId(score.getPost().getId())
//                        .member(score.getMember())
                        .createdAt(score.getCreatedAt())
                        .modifiedAt(score.getModifiedAt())
                        .build()
        );
    }


    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

//    @Transactional
//    public Member validateMember(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
//            return null;
//        }
//        return tokenProvider.getMemberFromAuthentication();
//    }

}
