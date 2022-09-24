package com.example.week08.repository;

import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import com.example.week08.dto.response.PostResponseDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.function.Predicate;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    List<Post> findAllByOrderByModifiedAtDesc();
//    List<Post> findAllByMember(Member member);

    @Override
    List<Post> findAll(Specification<Post> spec);

}
