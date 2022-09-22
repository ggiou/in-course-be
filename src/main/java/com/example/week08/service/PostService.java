package com.example.week08.service;

import com.example.week08.domain.Post;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.PostRepository;
import com.example.week08.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;

    // 코스 게시글 작성
    @Transactional
    public Post postCreate(PostRequestDto postRequestDto, MultipartFile image) throws IOException {

        String postImage = s3Uploader.upload(image, "static");
        Post post = new Post(postRequestDto, postImage);
        return postRepository.save(post);
    }

    // 코스(게시글) 단건 조회
    @Transactional
    public PostResponseDto getPost(Long courseId) {
        Post post = postRepository.findById(courseId).orElseThrow(
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


    // 코스 게시글 수정
    @Transactional
    public Post postUpdate(Long courseId, PostRequestDto postRequestDto, MultipartFile image) throws IOException {
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
        post.update(postRequestDto, imageUrl);

        return post;
    }

    // 코스(게시글) 삭제
    @Transactional
    public void postDelete(Long courseId) {
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
        postRepository.deleteById(courseId);
    }

}