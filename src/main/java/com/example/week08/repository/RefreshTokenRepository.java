package com.example.week08.repository;

import com.example.week08.domain.Member;
import com.example.week08.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>  {
    Optional<RefreshToken> findByMember(Member member);
}
