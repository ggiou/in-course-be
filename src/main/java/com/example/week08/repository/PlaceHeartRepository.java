package com.example.week08.repository;

import com.example.week08.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlaceHeartRepository extends JpaRepository<PlaceHeart, Long> {

    Optional<PlaceHeart> findByPlaceAndMember(Place place, Member member);

    // Heart Count 구하기
    @Query(value = "SELECT COUNT(p.heart) FROM PlaceHeart p WHERE p.place.id = :PlaceId")
    int findCountHeart(Long PlaceId);

}
