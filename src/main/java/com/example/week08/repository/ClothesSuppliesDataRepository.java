package com.example.week08.repository;

import com.example.week08.domain.ClothesSupplies;
import com.example.week08.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClothesSuppliesDataRepository extends JpaRepository<ClothesSupplies, Long> {
    Optional<ClothesSupplies> findByMember(Member member);

}
