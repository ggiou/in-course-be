package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.dto.TokenDto;
import com.example.week08.dto.request.NaverMemberInfoDto;
import com.example.week08.jwt.TokenProvider;
import com.example.week08.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverLoginApi {

    private String baseUrl = "https://nid.naver.com/oauth2.0/";
    private String nidMeUrl ="https://openapi.naver.com/v1/nid/me";
    private String authUrl = "authorize?client_id=";
//    private String authUrl2="&response_type=code&redirect_uri=http://localhost:3000/auth/naver&state=";
    private String authUrl3="&response_type=code&redirect_uri=http://localhost:8080/api/member/naver&state=";
    private String tokenUrlId ="token?client_id=";
    private String tokenUrlSecret = "&client_secret=";
    private String tokenUrlState="&grant_type=authorization_code&state=";
    private String tokenUrlCode ="&code=";

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    String ClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    String ClientSecret;
    String temp_state="123";

    private final MemberRepository memberRepo;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;


    // 네이버에서 제공하는 로그인창 url
    public String makeLoginUrl(){
        String loginUrl = baseUrl+authUrl+ClientId+authUrl3+temp_state;
        //https://nid.naver.com/oauth2.0/authorize?client_id=X7Ek1tyoUOUuuk_xRNjx&response_type=code&redirect_uri=http://localhost:8080/api/member/naver&state=123

        return loginUrl;
    }

    // 네이버에서 제공하는 token발급 url
    public String makeAuthUrl(String code,String state){
        String authUrl = baseUrl+tokenUrlId+ClientId+tokenUrlSecret+ClientSecret+tokenUrlState+state+tokenUrlCode+code;
        return authUrl;
    }

    //네이버 로그인후 토큰받기
    public NaverMemberInfoDto login(String code, String state, HttpServletResponse response){
        RestTemplate restTemplate =new RestTemplate(); // 토큰을 발급 받을 URL 초기화
        HttpEntity<?> httpentity = new HttpEntity<>(new HttpHeaders());


        ResponseEntity<Map> resultMap = restTemplate.exchange(makeAuthUrl(code,state), HttpMethod.GET,httpentity,Map.class); // GET 방식으로 토큰 발급 요청
        String NaverAccessToken=String.valueOf(resultMap.getBody().get("access_token")); // 받은 object를 String으로 변환
        String NaverRefreshToken=String.valueOf(resultMap.getBody().get("refresh_token"));
        String NaverTokenType=String.valueOf(resultMap.getBody().get("token_type")); // = "Bearer"
        String NaverExpiresIn=String.valueOf(resultMap.getBody().get("expires_in")); // = "3600"

        // 정보받기
        response.addHeader("RefreshToken",NaverRefreshToken);
        Map userData=nidMe(NaverAccessToken,NaverTokenType);
        // 받은 정보로 회원테이블 조회 및 신규 회원일시 자동 가입
        signIn(userData);

        // 로그인한 회원 객체 생성


        String id = String.valueOf(userData.get("id"));
        Member member = memberRepo.findByNaverId(id).get();



        // 토큰생성
        TokenDto token = tokenProvider.generateTokenDto(member);


        // 생성된 토큰 해더에 추가
        response.addHeader("Authorization","Bearer "+token.getAccessToken());
        System.out.println("accesstoken : "+token.getAccessToken());

        //
        String msg = member.getNickname()+"님 반갑습니다.";

        return new NaverMemberInfoDto(member.getNaverId(),member.getGender(), member.getNickname(), member.getEmail(), member.getProfileImage());

    }
// BE 시작점

    //네이버 로그인 기반 유저 정보 받기
    public Map nidMe(String NaverAccessToken,String NaverTokenType){
        RestTemplate restTemplate = new RestTemplate(); // 정보를 발급 받을 URL 초기화

        HttpHeaders headers = new HttpHeaders(); // URL에 보낼 헤더 초기화
        String nidMeHeader=NaverTokenType+" "+NaverAccessToken; // 헤더에 토큰 담기
        headers.set("Authorization",nidMeHeader);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfo = restTemplate.exchange(nidMeUrl,HttpMethod.GET,httpEntity, Map.class); // 정보 발급 요청
        Map userData = (Map) userInfo.getBody().get("response");
        //System.out.println(userData.get("id"));
        return userData;
    }

    //유저 정보 기반 회원테이블 생성
    public void signIn(Map userData){
        String naverId = String.valueOf(userData.get("id"));
        String name =String.valueOf(userData.get("nickname"));
        if(Objects.equals(name, "null")){
            name = String.valueOf(userData.get("name"));
        }
        String profile_image = String.valueOf(userData.get("profile_image"));
        String email = String.valueOf(userData.get("email"));
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);
        if(memberRepo.findByNaverId(naverId).isEmpty()){
            Member member = Member.builder()
                    .naverId(naverId)
                    .nickname(name)
                    .password(encodedPassword)
                    .profileImage(profile_image)
                    .email(email)
                    .emailAuth(1)
                    .build();
            memberRepo.save(member);
        }
        String msg = name+"님 환영합니다.";
    }

}
