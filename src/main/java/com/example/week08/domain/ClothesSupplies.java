package com.example.week08.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClothesSupplies extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @Column
    private String hat;

    @Column
    private String top;

    @Column
    private String outerwear;

    @Column
    private String pants;

    @Column
    private String shoes;

    @Column
    private String supplies;

    @Column
    private String supplies2;


    public void update(String hat, String top,String outerwear, String pants, String shoes, String supplies, String supplies2){
        this.hat = hat;
        this.top = top;
        this.outerwear = outerwear;
        this.pants = pants;
        this.shoes = shoes;
        this.supplies = supplies;
        this.supplies2 = supplies2;
    }
}
