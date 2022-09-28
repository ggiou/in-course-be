package com.example.week08.service;

import com.example.week08.domain.MailAuth;
import com.example.week08.domain.Member;
import com.example.week08.dto.request.MailRequestDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.MailAuthRepository;
import com.example.week08.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailAuthRepository mailAuthRepository;
    private final MemberRepository memberRepository;

    @Value("${spring.mail.username}")
    private String id;

    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++){ //6가지 숫자 인증 코드
            key.append((random.nextInt(10)));
        }
        return key.toString();
    } // 인증코드 랜덤 생성 과정

    @Transactional
    public void sendSignupMail(MailRequestDto mailRequestDto){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String subject = "InCourse[인싸들의 모임 코스] 회원 가입 인증 코드";
        String key = createKey();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // use multipart (true)

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(new InternetAddress(id, "InCourse_Admin"));
            mimeMessageHelper.setTo(mailRequestDto.getEmail());
            mimeMessageHelper.setText("<html>" +
                            "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">InCourse 회원가입용 이메일 주소 확인</h1>" +
                            "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면의 이메일 인증코드에 입력해주세요.</p> " +
                            "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\">" +
                            "<table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\">" +
                            "<tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">" +
                            key+"</td></tr></tbody></table></div>" +
                            "</html>", true);
            javaMailSender.send(mimeMessage);

            Optional<MailAuth> mailAuth = mailAuthRepository.findByEmail(mailRequestDto.getEmail());
            if (mailAuth.isPresent()) {
                MailAuth authKey = mailAuth.get();
                mailAuthRepository.delete(authKey);
            }
            MailAuth mail = MailAuth.builder()
                    .email(mailRequestDto.getEmail())
                    .authkey(key)
                    .build();
            mailAuthRepository.save(mail);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.MAIL_SEND_FAIL);
        }
    }

    @Transactional
    public void confirmSignupMail(MailRequestDto mailRequestDto) {
        if (mailRequestDto.getAuthkey()==null){
            throw new BusinessException("회원가입 인증키가 입력되지 않았습니다.",ErrorCode.INVALID_INPUT_VALUE);
        }
        Optional<MailAuth> mailAuth = mailAuthRepository.findByEmail(mailRequestDto.getEmail());
        MailAuth authKey = mailAuth.get();

        if (!Objects.equals(mailRequestDto.getAuthkey(), authKey.getAuthkey())){
            throw new BusinessException(ErrorCode.MAIL_AUTH_INCORRECT);
        }

        Optional<Member> memberOptional = memberRepository.findByEmail(mailRequestDto.getEmail());
        Member authmember = memberOptional.get();

        authmember.setEmailAuth();
        mailAuthRepository.delete(authKey);
    }
}
