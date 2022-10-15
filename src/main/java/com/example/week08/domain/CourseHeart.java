package com.example.week08.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn
    private Post post;
    @ManyToOne
    @JoinColumn
    private Member member;


    public CourseHeart(Post post, Member member) {
        this.post = post;
        this.member = member;
    }
}

