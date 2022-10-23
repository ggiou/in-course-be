package com.example.week08.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

import static java.lang.Boolean.TRUE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CourseHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "Member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Member member;

    @Column
    private boolean heart;
    @JoinColumn(name = "Course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Post post;

    public CourseHeart(Post post, Member member) {
        this.post = post;
        this.member = member;
        this.heart = TRUE;
    }
}

