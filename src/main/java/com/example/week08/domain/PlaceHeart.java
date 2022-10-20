package com.example.week08.domain;

import lombok.*;

import javax.persistence.*;

import static java.lang.Boolean.TRUE;

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
    @JoinColumn(name = "place_id")
    private Place place;
    @ManyToOne
    @JoinColumn
    private Member member;

    @Column
    private boolean heart;


    public PlaceHeart(Place place, Member member) {
        this.place = place;
        this.member = member;
        this.heart = TRUE;
    }
}

