package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.dto.TokenDto;
import com.example.week08.dto.request.KakaoMemberInfoDto;
import com.example.week08.dto.request.NaverMemberInfoDto;
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

import static com.example.week08.errorhandler.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverMemberService {
    // https://nid.naver.com/oauth2.0/authorize?client_id=X7Ek1tyoUOUuuk_xRNjx&response_type=code&redirect_uri=http://localhost:8080/api/member/naver&state=123

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<NaverMemberInfoDto> naverLogin(String code, HttpServletResponse response) throws JsonProcessingException {
// 1. "인가 코드"로 전체 response 요청
        String accessToken = getAccessToken(code);
        System.out.println(accessToken.getBytes().toString()+"      45\n\n");
        // 2. response에 access token으로 카카오 api 호출
        NaverMemberInfoDto naverMemberInfo = getnaverMemberInfo(accessToken);
        System.out.println(naverMemberInfo.getEmail()+"       네이버 48.\n\n");
        // 3. 필요시에 회원가입
        Member naverUser = registerNaverUserIfNeeded(naverMemberInfo);

        // 4. 강제 로그인 처리
        forceLogin(naverUser);
        System.out.println(naverUser.getNaverId()+"      네이버 강제 로그인.\n\n");
        // 5. response Header에 JWT 토큰 추가
        TokenDto token = naverUsersAuthorizationInput(naverUser, response);
        System.out.println("네이버 로그인에 성공했습니다.\n\n");
        return ResponseEntity.ok(new NaverMemberInfoDto(token, naverUser));
    }

    private String getAccessToken(String code)throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "7Cl6W7UwRoO8Ag75ZlpV"); //프론트 클라이언트 ID, 시크릿 받아오기
        body.add("client_secret", "FM8I5KRIWK");
        body.add("redirect_uri", "http://192.168.1.32:3000/login"); //네이버 어디서 받아올지 프론트
        body.add("code", code);
        body.add("state", "911");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private NaverMemberInfoDto getnaverMemberInfo(String accessToken)throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverMemberInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverMemberInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String naverId = jsonNode.get("response").get("id").asText();
        System.out.println(naverId+"      112\n\n");

        String nickname = jsonNode.get("response").get("name").asText();

        System.out.println(nickname+"      115\n\n");

        String email = jsonNode.get("response").get("email").asText();
        String image = jsonNode.get("response").get("profile_image").asText();
        log.info("네이버 사용자 정보: id -> " + naverId + ", nickname -> " + nickname+ ", email -> " +email+", profile -> " +image );
        return new NaverMemberInfoDto(email, nickname, naverId, image);
    }

    private Member registerNaverUserIfNeeded(NaverMemberInfoDto naverMemberInfo) {
        // DB 에 중복된 naver Id 가 있는지 확인
        String naverId = naverMemberInfo.getNaverId();
        Member naverUser = memberRepository.findByNaverId(naverId)
                .orElse(null);
        if (naverUser == null) {
            // 회원가입
            String nickname = naverMemberInfo.getNickname();
            Optional<Member> optionalNicname = memberRepository.findByNickname(nickname);
            if(optionalNicname.isPresent()){
                nickname = nickname+ Random();
            }
            String email = naverMemberInfo.getEmail();
            Optional<Member> optionalEmail = memberRepository.findByEmail(email);
            if (optionalEmail.isPresent()){
                throw new BusinessException("이미 가입된 이메일 입니다. 네이버 로그인 대신 다른 로그인을 해주세요.",DUPLICATED_USER_EMAIL);
            }

            String image = naverMemberInfo.getImage(); // 이미지가 s3에 저장이 안 된 상태니..
            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            naverUser = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .password(encodedPassword)
                    .profileImage(image)
                    .naverId(naverId)
                    .emailAuth(1)
                    .build();
            memberRepository.save(naverUser);
            log.info(nickname + "님의 회원가입이 완료되었습니다.");
        }
        return naverUser;
    }

    private void forceLogin(Member naverUser) {
        UserDetails userDetails = new UserDetailsImpl(naverUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private TokenDto naverUsersAuthorizationInput(Member naverUser, HttpServletResponse response) {
        // response header에 token 추가
        TokenDto token = tokenProvider.generateTokenDto(naverUser);
        response.addHeader("Authorization", "BEARER" + " " + token.getAccessToken());
        response.addHeader("RefreshToken", token.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", token.getAccessTokenExpiresIn().toString());
        response.addHeader("User-email", naverUser.getEmail());

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
