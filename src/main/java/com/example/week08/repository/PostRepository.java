package com.example.week08.repository;

import com.example.week08.domain.Member;
import com.example.week08.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Page<Post> findAllByOrderByModifiedAtDesc(Pageable pageable);

    List<Post> findAllByMember(Member member);
    @Query(value = "SELECT c" +
            " FROM Post c" +
            " LEFT JOIN FETCH c.place p" +
            //          " ON p.id = c.post.id" +
            " WHERE  c.id = :CourseId")
    Optional<Post> findByJoinPlace(@Param("CourseId") Long CourseId);


    @Override
    List<Post> findAll(Specification<Post> spec);

    List<Post> findByNewPost(boolean newPost);


    // 검색, 키워드가 들어간 코스 제목, 내용, 카테고리 코스(게시글) 조회
    @Query(value = "SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% OR p.weather LIKE %:keyword% OR p.region LIKE %:keyword% OR p.season LIKE %:keyword% OR p.who LIKE %:keyword%")
    List<Post> findAllSearch(String keyword);


    // 회원 Email을 이용햐여 내가 쓴 코스(게시글) 조회
    List<Post> findAllByMemberEmail(String email);


}
