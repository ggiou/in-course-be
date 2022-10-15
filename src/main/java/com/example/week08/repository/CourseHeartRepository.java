package com.example.week08.repository;

import com.example.week08.domain.CourseHeart;
import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseHeartRepository extends JpaRepository<CourseHeart, Long> {

    Optional<CourseHeart> findByPostAndMember(Post post, Member member);

    List<CourseHeart> findAllByMemberEmail(String email);

    // Heart Count 구하기
    @Query(value = "SELECT COUNT(c.heart) FROM CourseHeart c WHERE c.post.id = :CourseId")
    int findCountHeart(Long CourseId);

}
