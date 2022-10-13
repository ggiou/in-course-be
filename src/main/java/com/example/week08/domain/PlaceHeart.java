package com.example.week08.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PlaceHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long placeId;
    @ManyToOne
    @JoinColumn
    private Member member;
    @Column
    private Boolean heart;

    public PlaceHeart(Long placeId, Member member) {
        this.placeId = placeId;
        this.member = member;
        this.heart = Boolean.TRUE;
    }
}

