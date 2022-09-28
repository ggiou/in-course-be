package com.example.week08.domain;


import com.example.week08.dto.request.ProfileRequestDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
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
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileImage;

    @Column
    private String location;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String naverId;

    @Column(nullable = false)
    private int emailAuth;

    @PrePersist
    public void prePersist(){
        this.location = this.location == null ? "현재 위치가 지정되어있지 않습니다." : this.location;
        this.nickname = this.nickname == null ? "현재 닉네임이 지정되어있지 않습니다." : this.nickname;
    }

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


    public void update(String nickname, String location, String newPassword, String imageUrl){
        this.nickname = nickname;
        this.location = location;
        this.password = newPassword;
        this.profileImage = imageUrl;
    }

    public void detialSignup(String nickname, String location){
        this.nickname = nickname;
        this.location = location;
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
