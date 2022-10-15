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

    @ManyToOne
    @JoinColumn
    private Place place;
    @ManyToOne
    @JoinColumn
    private Member member;


    public PlaceHeart(Place place, Member member) {
        this.place = place;
        this.member = member;
    }
}

