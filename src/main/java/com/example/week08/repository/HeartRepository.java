package com.example.week08.repository;

import com.example.week08.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByPostIdAndNickname(Long Id , String nickname);
    List<Heart> findAllByPostId(Long postId);
}
