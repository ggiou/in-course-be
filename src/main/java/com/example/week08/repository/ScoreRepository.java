package com.example.week08.repository;

import com.example.week08.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScoreRepository extends JpaRepository<Score, Long> {
}
