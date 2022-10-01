package com.example.week08.repository;

import com.example.week08.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    List<Heart> findByNickname (String nickname);
}
