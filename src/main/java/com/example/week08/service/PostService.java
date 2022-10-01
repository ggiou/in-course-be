package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import com.example.week08.dto.request.PlacePutDto;
import com.example.week08.dto.request.PostPlaceDto;
import com.example.week08.dto.request.PostPlacePutDto;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.PostRepository;
import com.example.week08.repository.PostSpecification;
import com.example.week08.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;
    private final PlaceService placeService;

    // 코스 게시글 작성
    @Transactional
    public Post postCreate(PostPlaceDto postPlaceDto, MultipartFile image, Member member) throws IOException {
        String postImage = s3Uploader.upload(image, "static");
        Post post = new Post(postPlaceDto.getPostRequestDto(), postImage, member);
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

    // 코스(게시글) 검색(제목, 내용, 카테고리 검색)
    @Transactional
    public List<PostResponseDto> searchPost(String keyword){
        return postRepository.findAllSearch(keyword)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 코스 게시글 수정
    @Transactional
    public Post postUpdate(Long courseId, PostPlacePutDto postPlacePutDto, MultipartFile image, Member member) throws IOException {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

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
    public void postDelete(Long courseId, Member member) throws UnsupportedEncodingException {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        String imageUrl = post.getImage();
        String deleteUrl = imageUrl.substring(imageUrl.indexOf("static"));
        s3Uploader.deleteImage(deleteUrl);
        postRepository.deleteById(courseId);
    }

}