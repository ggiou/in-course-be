package com.example.week08.dto.response;

import com.example.week08.domain.ClothesSupplies;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesSuppliesResponseDto {
    private String member;
    private String hat;
    private String top;
    private String outerwear;
    private String pants;
    private String shoes;
    private String supplies;
    private String supplies2;

    public ClothesSuppliesResponseDto(ClothesSupplies data){
        this.member = data.getMember().getNickname();
        this.hat = data.getHat();
        this.top = data.getTop();
        this.outerwear = data.getOuterwear();
        this.pants = data.getPants();
        this.shoes = data.getShoes();
        this.supplies = data.getSupplies();
        this.supplies2 = data.getSupplies2();
    }
}
