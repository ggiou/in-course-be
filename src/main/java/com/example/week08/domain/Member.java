package com.example.week08.domain;


import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String email;

    @NotNull
    @Column(unique = true)
    private String nickname;
    @NotNull
    private String password;
    @Column
    private String profileImage;
    @Column
    private String location;
    @Column(unique = true)
    private Long kakaoId;
    @Column(unique = true)
    private String naverId;
    @NotNull
    private int emailAuth;
    @Column
    private String badge;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Member_id")
    private List<CourseHeart> courseHeart;

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "Member_id")
//    private List<Post> post;

    @PrePersist
    public void prePersist(){
        this.location = this.location == null ? "현재 위치가 지정되어있지 않습니다." : this.location;
        this.nickname = this.nickname == null ? UUID.randomUUID().toString() : this.nickname;
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
    public void badgeupdate(String badge){
        this.badge = badge;
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
