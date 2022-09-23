package com.example.week08.repository;

import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place,Long> {

    List<Place> findAllByPostId(Long postId);
}
