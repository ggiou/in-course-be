//package com.example.week08.service;
//
//import com.example.week08.domain.Heart;
//import com.example.week08.domain.Post;
//import com.example.week08.dto.response.ResponseDto;
//import com.example.week08.jwt.TokenProvider;
//import com.example.week08.repository.HeartRepository;
//import com.example.week08.repository.PostRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class HeartService {
//
//    private final PostRepository postRepository;
//    private final HeartRepository heartRepository;
//
////    private final TokenProvider tokenProvider;
//
//    // 코스 게시글 찜하기
//    @Transactional
//    public ResponseDto<?> addPostHeart(Long courseId, HttpServletRequest request) {
////        if (null == request.getHeader("RefreshToken")) {
////            return ResponseDto.fail("MEMBER_NOT_FOUND",
////                    "로그인이 필요합니다.");
////        }
////
////        if (null == request.getHeader("Authorization")) {
////            return ResponseDto.fail("MEMBER_NOT_FOUND",
////                    "로그인이 필요합니다.");
////        }
////        System.out.println("===============");
////        Member member = validateMember(request);
////        if (null == member) {
////            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
////        }
//        System.out.println("===============");
//        Post post = isPresentPost(courseId);
//        if (null == post) {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//        }
//
////        Heart heart = isPresentHeart(post.getId(), member.getNickname());
//
//        if (null == heart)
//            heartRepository.save(
//                    Heart.builder()
//                            .postId(post.getId())
////                            .nickname(member.getNickname())
//                            .build()
//            );
//        else
//            heartRepository.delete(heart);
//
//        post.syncHeart(heartRepository.findAllByPostId(post.getId()).size());
//
//        if (heart == null) {
//            return ResponseDto.success("찜하기가 정상적으로 반영되었습니다.");
//        } else {
//            return ResponseDto.success("찜하기가 삭제되었습니다.");
//        }
//
//    }
//
//
//    @Transactional(readOnly = true)
//    public Post isPresentPost(Long postId) {
//        Optional<Post> optionalPost = postRepository.findById(postId);
//        return optionalPost.orElse(null);
//    }
//
//
//    public Heart isPresentHeart(Long postId, String nickname) {
//        Optional<Heart> optionalHeart = heartRepository.findByPostIdAndNickname(postId, nickname);
//        return optionalHeart.orElse(null);
//    }
//
//
////    @Transactional
////    public Member validateMember(HttpServletRequest request) {
////        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
////            return null;
////        }
////        return tokenProvider.getMemberFromAuthentication();
////    }
//}