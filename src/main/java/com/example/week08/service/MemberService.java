package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.dto.TokenDto;
import com.example.week08.dto.request.LoginRequestDto;
import com.example.week08.dto.request.MemberRequestDto;
import com.example.week08.dto.response.MemberResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
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

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public void createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getEmail())) {
            throw new BusinessException("중복 이메일",ErrorCode.DUPLICATED_USER_EMAIL);
        }
        if (null != isPresentNickname(requestDto.getNickname())) {
            throw new BusinessException(ErrorCode.DUPLICATED_USER_NICKNAME);
        }
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new BusinessException(ErrorCode.PASSWORDS_NOT_MATCHED);
        }
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .emailAuth(0)
                .build();
        memberRepository.save(member);
    }

    public ResponseEntity<MemberResponseDto> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getEmail());
        if (null == member) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
            member.validatePassword(passwordEncoder, requestDto.getPassword());
        if (member.getEmailAuth() == 0){
            throw new BusinessException(ErrorCode.MEMBER_NOT_EMAIL_AUTH);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response, requestDto);
        return ResponseEntity.ok(new MemberResponseDto(tokenDto));
    }

    public ResponseEntity<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            throw new BusinessException(ErrorCode.JWT_NOT_PERMIT);
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member)
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        return tokenProvider.deleteRefreshToken(member);
    }


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
