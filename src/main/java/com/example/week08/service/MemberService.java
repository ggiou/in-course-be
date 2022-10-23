package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.dto.TokenDto;
import com.example.week08.dto.request.LoginRequestDto;
import com.example.week08.dto.request.MemberDetailRequestDto;
import com.example.week08.dto.request.MemberRequestDto;
import com.example.week08.dto.response.MemberResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.jwt.TokenProvider;
import com.example.week08.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.week08.errorhandler.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public ResponseEntity<MemberResponseDto> createMember(MemberRequestDto requestDto) {
        Optional<Member> optionalEmail = memberRepository.findByEmail(requestDto.getEmail());
        if (optionalEmail.isPresent()) {
            Member existingMemberd = optionalEmail.get();
            if (optionalEmail.isPresent() && existingMemberd.getKakaoId() != null) {
                throw new BusinessException("이미 카카오로 가입한 이메일입니다. 새로운 이메일로 가입하거나 카카오 로그인을 해주세요.", DUPLICATED_USER_EMAIL);
            } //카카오 회원가입 유저 확인
            if (optionalEmail.isPresent() && existingMemberd.getNaverId() != null) {
                throw new BusinessException("이미 네이버로 가입한 이메일입니다. 새로운 이메일로 가입하거나 네이버 로그인을 해주세요.", DUPLICATED_USER_EMAIL);
            }//네이버 회원가입 유저 확인
            if (null != isPresentMember(requestDto.getEmail())) {
                throw new BusinessException("이미 존재하는 이메일입니다.", DUPLICATED_USER_EMAIL);
            }//이메일 회원가입 유저 확인
        }
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new BusinessException(PASSWORDS_NOT_MATCHED);
        }
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .emailAuth(0)
                .build();
        memberRepository.save(member);
        return ResponseEntity.ok(new MemberResponseDto(true, requestDto.getEmail()+"님의 회원가입에 성공하였습니다.",member));
    } //회원 가입

    @Transactional
    public ResponseEntity<MemberResponseDto> detailMember(MemberDetailRequestDto requestDto) {
        Member member = isPresentMember(requestDto.getEmail());
        if (null != isPresentNickname(requestDto.getNickname())) {
            throw new BusinessException(DUPLICATED_USER_NICKNAME);
        }
        String nickname = requestDto.getNickname();
        String location = requestDto.getLocation();
        String gender = requestDto.getGender();
        member.detialSignup(nickname, location, gender);
        return ResponseEntity.ok(new MemberResponseDto(true, requestDto.getNickname()+"님의 회원정보 저장이 성공하였습니다.",member));
    } //회원 가입 상세(지역, 닉네임)

    public ResponseEntity<MemberResponseDto> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getEmail());
        if (null == member) {
            throw new BusinessException(MEMBER_NOT_FOUND);
        }
            member.validatePassword(passwordEncoder, requestDto.getPassword());
        if (member.getEmailAuth() == 0){
           return ResponseEntity.ok(new MemberResponseDto(member));
//            throw new BusinessException(MEMBER_NOT_EMAIL_AUTH);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response, requestDto);
        return ResponseEntity.ok(new MemberResponseDto(tokenDto, member));
    } //로그인

    public ResponseEntity<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            throw new BusinessException(JWT_NOT_PERMIT);
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member)
            throw new BusinessException(MEMBER_NOT_FOUND);
        return tokenProvider.deleteRefreshToken(member);
    } //로그아웃


    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
            return optionalMember.orElse(null);
    }

    @Transactional(readOnly = true)
    public Member isPresentNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    private void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response, LoginRequestDto requestDto) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
        response.addHeader("User-email", requestDto.getEmail());
    }


}
