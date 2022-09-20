package com.example.week08.domain;


import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String profileImage;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private Long naverId;

    @Column(nullable = false)
    private int emailAuth;

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (o == null || Hibernate.getClass(this)!= Hibernate.getClass(o)){
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    public void setEmailAuth(){this.emailAuth = 1;} //이메일 인증 완료 된 상태
    @Override
    public int hashCode(){return getClass().hashCode();}

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new BusinessException("로그인이 실패하였습니다, 비밀번호를 확인해 주세요.", ErrorCode.LOGIN_INPUT_INVALID);
        }
    }
}
