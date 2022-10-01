package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import com.example.week08.dto.request.*;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.MemberRepository;
import com.example.week08.repository.PlaceRepository;
import com.example.week08.repository.PostRepository;
import com.example.week08.repository.PostSpecification;
import com.example.week08.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;
    private final PlaceService placeService;
    private final MemberRepository memberRepository;
    private final Post post;

    // 코스 게시글 작성
    @Transactional
    public Post postCreate(PostPlaceDto postPlaceDto, MultipartFile image) throws IOException {

        String postImage = s3Uploader.upload(image, "static");
        Post post = new Post(postPlaceDto.getPostRequestDto(), postImage);

        Long courseId = postRepository.save(post).getId();

        for (int i =0; i <postPlaceDto.getPlaceRequestDtoList().size(); i++){

            placeService.placeCreate(courseId, postPlaceDto.getPlaceRequestDtoList().get(i)
//                    , member
            );
        }


        return post;
    }

    // 코스(게시글) 단건 조회
    @Transactional
    public PostResponseDto getPost(Long courseId) {
//        Post post = postRepository.findById(courseId).orElseThrow(
//                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
//        );
        Post post = postRepository.findByJoinPlace(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        return new PostResponseDto(post);
    }


    // 코스(게시글) 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPost() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 코스(게시글) 카테고리 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> findPost(PostRequestDto requestDto) {
        Map<String, Object> searchKeys = new HashMap<>();
        if (requestDto.getWeather() != null) searchKeys.put("weather", requestDto.getWeather());
        if (requestDto.getRegion() != null) searchKeys.put("region", requestDto.getRegion());
        if (requestDto.getSeason() != null) searchKeys.put("season", requestDto.getSeason());
        if (requestDto.getWho() != null) searchKeys.put("who", requestDto.getWho());

        return postRepository.findAll(PostSpecification.searchPost(searchKeys))
                .stream()
                .map(p -> new PostResponseDto((Post) p))
                .collect(Collectors.toList());
    }


    // 코스 게시글 수정
    @Transactional
    public Post postUpdate(Long courseId, PostPlacePutDto postPlacePutDto, MultipartFile image) throws IOException {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
//        if (!post.getMember().getId().equals(member.getId())) {
//            throw new IllegalArgumentException("수정 권한이 없습니다.");
//        }

         String imageUrl = post.getImage();
        //이미지 존재시 먼저 삭제후 다시 업로드.
        if (imageUrl != null) {
            String deleteUrl = imageUrl.substring(imageUrl.indexOf("static"));
            s3Uploader.deleteImage(deleteUrl);
            imageUrl = s3Uploader.upload(image, "static");
        }
        post.update(postPlacePutDto.getPostRequestDto(), imageUrl);

        for (int i =0; i <postPlacePutDto.getPlacePutDtoList().size(); i++){

            PlacePutDto place = postPlacePutDto.getPlacePutDtoList().get(i);
            placeService.placeUpdate(courseId, place
//                    , member
            );
        }

        return post;
    }

    // 코스(게시글) 삭제
    @Transactional
    public void postDelete(Long courseId) throws UnsupportedEncodingException {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
//        if (!post.getMember().getId().equals(member.getId())) {
//            throw new IllegalArgumentException("삭제 권한이 없습니다.");
//        }

        String image = post.getImage();
        String deleteUrl = image.substring(image.indexOf("static")); //이미지
        //s3에서 이미지 삭제
        s3Uploader.deleteImage(deleteUrl);
        //포스트 아이디가 같은 카드 가져오기

        postRepository.deleteById(courseId);
    }

    // 메인 새로운게시물/날씨/지역/계절/평점 기반 추천
    //newpost true인 게시물만 가져옴
    //그중 각 카테고리에 맞는 게시물을 가져옴
    //avgScore값순으로 내림차순정렬하고 그중 첫번째 게시물을 조회
//    @Transactional(readOnly = true)
//    public List<PostResponseDto> getRecommended() {
//        //게시물의 newpost값이 true인 녀석들만 가져옴
//        //비로그인시에도 추천을 해주려면 맴버에 저장되면 안됨
//        //저장하지 않고 데이터를 바로 받아오는것도 생각해봐야함
//        Map<String, Object> searchKeys = new HashMap<>();
//        if (post.isNewPost()) searchKeys.put("newpost", post.isNewPost()); //새로운 게시물
//        if (member.getWeather() != null) searchKeys.put("weather", member.getWeather());//날씨
//        if (member.getRegion() != null) searchKey.put("region", member.getRegion());//지역
//        if (member.getSeason() != null) searchKeys.put("season", member.getSeason());//계절
//        if (topAvgScore(post) != null) searchKeys.put("topAvgScore", topAvgScore(post));//평점
//
//        return postRepository.findAll(PostSpecification.searchPost(searchKeys))
//                .stream()
//                .map(PostResponseDto::new)
//                .collect(Collectors.toList());
//    }

    //메인 비로그인 유저용 새로운게시물/ 평점기반
    @Transactional(readOnly = true)
    public List<PostResponseDto> getCommonRecommended() {
        Map<String, Object> searchKeys = new HashMap<>();
        if (post.isNewPost()) searchKeys.put("newpost", post.isNewPost()); //새로운 게시물
        if (topAvgScore(post) != null) searchKeys.put("topAvgScore", topAvgScore(post));//평점

        return postRepository.findAll(PostSpecification.searchPost(searchKeys))
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Post> topAvgScore(Post post){
        List<Post> posts = postRepository.findAll();
        int top = 0;
        List<Post> topAvgPost = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++){
            if (post.getAvgScore()>top){
                top = post.getAvgScore();
                topAvgPost.clear();
                topAvgPost.add(posts.get(i));
                }
        }
    return topAvgPost;
    }

}