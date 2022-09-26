package com.example.week08.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String image;

//    @JoinColumn(name = "place_id", nullable = false)
//    @OneToMany(fetch = FetchType.LAZY)
//    private Place place;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String tag;

    @Column
    private int score;

    @Column(nullable = false)
    private int heart;

//    @JoinColumn(name = "member_id", nullable = false)
   @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

//    // 회원정보 검증
//    public boolean validateMember(Member member) {
//        return !this.member.equals(member);
//    }

    // 찜하기 상태 동기화
    public void syncHeart(int num) {
        this.heart = (num);
    }

}
