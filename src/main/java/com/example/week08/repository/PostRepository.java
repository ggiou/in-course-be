package com.example.week08.repository;

import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
//    List<Post> findAllByMember(Member member);
}
