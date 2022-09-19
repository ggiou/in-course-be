package com.example.week08.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.week08.domain.Post;
import com.example.week08.domain.Score;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.dto.request.ScoreRequestDto;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.dto.response.ResponseDto;
import com.example.week08.dto.response.ScoreResponseDto;
import com.example.week08.repository.PostRepository;
import com.example.week08.repository.ScoreRepository;
import com.example.week08.util.AwsS3UploadService;
import com.example.week08.util.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
//    private final TokenProvider tokenProvider;
    private final UploadService s3Service;

    private final AwsS3UploadService awsS3UploadService;

    // 코스 게시글 작성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto,
                                       MultipartFile file,
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

        String fileName = createFileName(file.getOriginalFilename());  // 파일 이름을 유니크한 이름으로 재지정. 같은 이름의 파일을 업로드 하면 overwrite 됨
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        ResponseDto.success(s3Service.getFileUrl(fileName));

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .image(s3Service.getFileUrl(fileName))
                .category(requestDto.getCategory())
                .tag(requestDto.getTag())
                .heart(0)
//                .member(member)
                .build();

        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .image(post.getImage())
                        .category(post.getCategory())
                        .tag(post.getTag())
                        .heart(post.getHeart())
//                        .member(post.getMember())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    // 코스 게시글 단건 조회
    @Transactional
    public ResponseDto<?> getPost(Long postId) {
        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .image(post.getImage())
                        .category(post.getCategory())
                        .tag(post.getTag())
                        .score(post.getScore())
                        .heart(post.getHeart())
//                        .member(post.getMember())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }


    // 코스 전체 게시글 조회
    @Transactional
    public ResponseDto<?> getAllPost() {
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseAllDto = new ArrayList<>();
        for (Post post : postList) {
            postResponseAllDto.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .image(post.getImage())
                            .category(post.getCategory())
                            .tag(post.getTag())
                            .score(post.getScore())
                            .heart(post.getHeart())
//                        .member(post.getMember())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(postResponseAllDto);
    }


    // 코스 게시글 수정
    @Transactional
    public ResponseDto<?> updatePost(Long id,
                                     PostRequestDto requestDto,
                                     MultipartFile file,
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

        // 게시글 호출
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

//        if (post.validateMember(member)) {
//            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
//        }


        String fileName = createFileName(file.getOriginalFilename());  // 파일 이름을 유니크한 이름으로 재지정. 같은 이름의 파일을 업로드 하면 overwrite 됨
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        ResponseDto.success(s3Service.getFileUrl(fileName));

        awsS3UploadService.deleteFile(getFileNameFromURL(post.getImage()));  // 기존 파일 삭제

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setImage(s3Service.getFileUrl(fileName));
        post.setCategory(requestDto.getCategory());
        post.setTag(requestDto.getTag());

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .image(post.getImage())
                        .category(post.getCategory())
                        .tag(post.getTag())
                        .score(post.getScore())
                        .heart(post.getHeart())
//                        .member(post.getMember())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
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

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

//        if (post.validateMember(member)) {
//            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
//        }

        postRepository.delete(post);

        awsS3UploadService.deleteFile(getFileNameFromURL(post.getImage()));

        return ResponseDto.success("delete success");
    }



    // URL 에서 파일이름(key) 추출
    public static String getFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
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

