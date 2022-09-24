package com.example.week08.repository;

import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
//    List<Post> findAllByMember(Member member);
    @Query(value = "SELECT c" +
            " FROM Post c" +
            " LEFT JOIN FETCH c.place p" +
            //          " ON p.id = c.post.id" +
            " WHERE  c.id = :CourseId")
    Optional<Post> findByJoinPlace(@Param("CourseId") Long CourseId);
}
