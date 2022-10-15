package com.example.week08.repository;

import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import com.example.week08.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ScoreRepository extends JpaRepository<Score, Long> {

    // 평균 Score 구하기
    @Query(value = "SELECT AVG(s.score) FROM Score s WHERE s.post.id = :CourseId")
    double findAvgScore(Long CourseId);

    // 중복 Score 검사
    Optional<Score> findScoreByPostAndMember(Post post, Member member);

    // 중복 Score 삭제
    void deleteByPostAndMember(Post post, Member member);

}
