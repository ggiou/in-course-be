package com.example.week08.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.week08.domain.Post;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.PostRepository;
import com.example.week08.util.AwsS3UploadService;
import com.example.week08.util.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    //    private final TokenProvider tokenProvider;
    private final UploadService s3Service;

    private final AwsS3UploadService awsS3UploadService;

    // 코스 게시글 작성
    @Transactional
    public Post postCreate(PostRequestDto postRequestDto, MultipartFile file) {

        // 파일 이름을 유니크한 이름으로 재지정. 같은 이름의 파일을 업로드 하면 overwrite 됨
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }

        String image = s3Service.getFileUrl(fileName);
        Post post = new Post(postRequestDto, image);
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
    public Post postUpdate(Long courseId, PostRequestDto postRequestDto, MultipartFile file) {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
//        if (!post.getMember().getId().equals(member.getId())) {
//            throw new IllegalArgumentException("수정 권한이 없습니다.");
//        }

        // 파일 이름을 유니크한 이름으로 재지정. 같은 이름의 파일을 업로드 하면 overwrite 됨
        ObjectMetadata objectMetadata = new ObjectMetadata();
        String fileName = createFileName(file.getOriginalFilename());
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }

        // 기존 이미지 파일 삭제
        awsS3UploadService.deleteFile(getFileNameFromURL(post.getImage()));

        String image = s3Service.getFileUrl(fileName);
        post.update(postRequestDto, image);

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

        awsS3UploadService.deleteFile(getFileNameFromURL(post.getImage()));
        postRepository.deleteById(courseId);

    }


    // URL 에서 파일이름(key) 추출
    public static String getFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }


    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
        }
    }

}