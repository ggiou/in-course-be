package com.example.week08.service;

import com.example.week08.domain.Heart;
import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import com.example.week08.dto.request.ProfileRequestDto;
import com.example.week08.dto.response.MyHeartListDto;
import com.example.week08.dto.response.MyPostListResponseDto;
import com.example.week08.dto.response.ProfileResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.jwt.TokenProvider;
import com.example.week08.repository.HeartRepository;
import com.example.week08.repository.MemberRepository;
import com.example.week08.repository.PostRepository;
import com.example.week08.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;

    private final S3Uploader s3Uploader;

    public ProfileResponseDto getProfile(HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            throw new BusinessException("토큰이 유효하지 않습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }
        return new ProfileResponseDto(member);
    } //회원(내) 프로필 정보 보기

    public MyPostListResponseDto getPost(HttpServletRequest request) throws BusinessException {
        Member member = validateMember(request);
        if (null == member) {
            throw new BusinessException(member.getNickname() + "의 작성한 글을 볼 권한이 없습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }
        List<Post> myPostList = postRepository.findAllByMember(member);
        return new MyPostListResponseDto(myPostList);
    } //회원(내) 작성한 글 전체 보기

    public MyHeartListDto getHeart(HttpServletRequest request) throws BusinessException {
        Member member = validateMember(request);
        if (null == member) {
            throw new BusinessException(member.getNickname() + "의 찜한 코스를 볼 권한이 없습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }
        List<Heart> hearts = heartRepository.findByNickname(member.getNickname());
        List<Optional<Post>> heartList = new ArrayList<>();

        for (Heart heart : hearts) {
            heartList.add(postRepository.findById(heart.getPostId()));
        }

        return new MyHeartListDto(heartList);

    } //회원(내) 찜한 코스 전체 보기

    private Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    } //유효한 회원인지 확인

    @Transactional
    public ProfileResponseDto updateProfile(ProfileRequestDto profileRequestDto, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (null == member) {
            throw new BusinessException("수정 권한이 없습니다.", ErrorCode.JWT_INVALID_TOKEN);
        }
//        if (member.getNaverId() != null || member.getKakaoId() != null) {
//            throw new BusinessException("네이버, 카카오로 로그인 한 회원은 정보를 수정할 수 없습니다.", ErrorCode.HANDLE_ACCESS_DENIED);
//        }


        String imageUrl = member.getProfileImage();
        System.out.println(imageUrl + "             이미지 url\n\n");
        //이미지 존재시 먼저 삭제후 다시 업로드.


        if (profileRequestDto.getImage() == null || profileRequestDto.getImage().isEmpty()) {
            imageUrl = member.getProfileImage();
            System.out.println(imageUrl + "             마이페이지 97\n\n");

        } else {
            if (imageUrl != null && imageUrl.contains("profile")) {
                String deleteUrl = imageUrl.substring(imageUrl.indexOf("profile"));
//            deleteUrl = decode
                System.out.println(deleteUrl + "                 삭제 URL\n\n");
                s3Uploader.deleteImage(deleteUrl);
            }

            imageUrl = s3Uploader.upload(profileRequestDto.getImage(), "profile");
            System.out.println(imageUrl + "             마이페이지 101\n\n");
        }
        //만약 수정할려는 이미지(이미지 입력이 없으면 무저건 null 반환하게 해둠)

        String nickname = profileRequestDto.getNickname();
        if (nickname == null || nickname.isEmpty()) {
            nickname = member.getNickname();
        }

        String location = profileRequestDto.getLocation();
        if (location == null || location.isEmpty()) {
            location = member.getLocation();
        }

        String newPassword;
        String passwordConfirm;
        String newPasswords = profileRequestDto.getPassword();
        String passwordConfirms = profileRequestDto.getPasswordConfirm();
        if (newPasswords == null || newPasswords.isEmpty()) {
            newPassword = member.getPassword();
            passwordConfirm = newPassword;
            System.out.println(newPassword + "\n" + passwordConfirm + "                   비었을 떄\n\n");
        } //수정 때, 새 비밀번호 입력 안 할시 기존 비밀번호 가져와서, confirm도 설정
        else {
            newPassword = newPasswords;
            passwordConfirm = passwordConfirms;
            if (!Objects.equals(newPassword, passwordConfirm)) {
                throw new BusinessException(ErrorCode.PASSWORDS_NOT_MATCHED);
            }
            newPassword = passwordEncoder.encode(newPasswords);
            System.out.println(newPassword + "\n" + passwordConfirm + "                 비밀번호 변경 할 때\n\n");
        } //수정 때, 비밀번호를 어쨌든 암호화 해줘야 함


        member.update(nickname, location, newPassword, imageUrl);
        memberRepository.save(member);

        return new ProfileResponseDto(member);
    } //회원(내) 정보 수정하기

}
