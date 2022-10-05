package com.example.week08.repository;

import com.example.week08.domain.Member;
import com.example.week08.domain.OpenWeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpenWeatherDataRepository extends JpaRepository<OpenWeatherData,Long> {
    Optional<OpenWeatherData> findByMember(Member member);
}
