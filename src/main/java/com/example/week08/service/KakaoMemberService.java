package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.dto.TokenDto;
import com.example.week08.dto.request.KakaoMemberInfoDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.jwt.TokenProvider;
import com.example.week08.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.example.week08.errorhandler.ErrorCode.DUPLICATED_USER_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoMemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<KakaoMemberInfoDto> kakakoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 전체 response 요청
        String accessToken = getAccessToken(code);
        System.out.println(accessToken.getBytes().toString()+"      카카오 인가 코드.\n\n");
        // 2. response에 access token으로 카카오 api 호출
        KakaoMemberInfoDto kakaoMemberInfo = getkakaoMemberInfo(accessToken);

        // 3. 필요시에 회원가입
        Member kakaoUser = registerKakaoUserIfNeeded(kakaoMemberInfo);

        // 4. 강제 로그인 처리
        forceLogin(kakaoUser);
        System.out.println(kakaoUser.getKakaoId()+"      카카오 강제 로그인.\n\n");
        // 5. response Header에 JWT 토큰 추가
        TokenDto token = kakaoUsersAuthorizationInput(kakaoUser, response);
        System.out.println("카카오 로그인에 성공했습니다.\n\n");
        return ResponseEntity.ok(new KakaoMemberInfoDto(token, kakaoUser));
    }


    private String getAccessToken(String code)throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "58a918ce1a7631c2f849c76b91744d19"); //프론트 클라이언트 ID, 시크릿 받아오기
        body.add("client_secret", "LITCGKUE456jAiblUawF5d1NEgFeZ4eP");
        body.add("redirect_uri", "http://192.168.1.32:3000/kakao"); //카카오 어디서 받아올지 프론트
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        System.out.println(kakaoTokenRequest.getBody().toString()+"카카오 85");

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoMemberInfoDto getkakaoMemberInfo(String accessToken)throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoMemberInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoMemberInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("kakao_account").get("profile").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String image = jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText();
        log.info("카카오 사용자 정보: id -> " + id + ", nickname -> " + nickname+ ", email -> " +email+", profile -> " +image );
        return new KakaoMemberInfoDto(id, nickname, email, image);
    }

    private Member registerKakaoUserIfNeeded(KakaoMemberInfoDto kakaoMemberInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoMemberInfo.getKakaoId();
        Member kakaoUser = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
            // 회원가입
            // nickname: kakao nickname
            String nickname = kakaoMemberInfo.getNickname();

            Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
            if(optionalMember.isPresent()){
                nickname = nickname+ Random();
            }

            //email : kakao email
            String email = kakaoMemberInfo.getEmail();
            Optional<Member> optionalEmail = memberRepository.findByEmail(email);
            if (optionalEmail.isPresent()){
                throw new BusinessException("이미 가입된 이메일 입니다. 카카오 로그인 대신 다른 로그인을 해주세요.",DUPLICATED_USER_EMAIL);
            }
            //image : kakao image
            String image = kakaoMemberInfo.getImage(); // 이미지가 s3에 저장이 안 된 상태니..
            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .password(encodedPassword)
                    .profileImage(image)
                    .kakaoId(kakaoId)
                    .emailAuth(1)
                    .build();
            memberRepository.save(kakaoUser);
            log.info(nickname + "님의 회원가입이 완료되었습니다.");
        }
        return kakaoUser;
    }

    private void forceLogin(Member kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private TokenDto kakaoUsersAuthorizationInput(Member kakaouser, HttpServletResponse response) {
        // response header에 token 추가
        TokenDto token = tokenProvider.generateTokenDto(kakaouser);
        response.addHeader("Authorization", "BEARER" + " " + token.getAccessToken());
        response.addHeader("RefreshToken", token.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", token.getAccessTokenExpiresIn().toString());
        response.addHeader("User-email", kakaouser.getEmail());

        return token;
    }

    public static String Random() {
        Random random = new Random();
        int length = random.nextInt(5)+5;

        StringBuffer newWord = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(3);
            switch(choice) {
                case 0:
                    newWord.append((char)((int)random.nextInt(25)+97));
                    break;
                case 1:
                    newWord.append((char)((int)random.nextInt(25)+65));
                    break;
                case 2:
                    newWord.append((char)((int)random.nextInt(10)+48));
                    break;
                default:
                    break;
            }
        }
        return newWord.toString();
    } // 인증코드 랜덤 생성 과정
}
// https://kauth.kakao.com/oauth/authorize?client_id=f027943b2d088d9a7d19768a8f540e9b&redirect_uri=http://localhost:8080/api/member/kakao&response_type=code
//client_id= 본인의 rest api 키