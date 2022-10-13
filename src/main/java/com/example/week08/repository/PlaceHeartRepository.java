package com.example.week08.repository;

import com.example.week08.domain.PlaceHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceHeartRepository extends JpaRepository<PlaceHeart, Long> {

    Optional<PlaceHeart> findHeartByPlaceIdAndMemberId(Long placeId, Long memberId);

}
