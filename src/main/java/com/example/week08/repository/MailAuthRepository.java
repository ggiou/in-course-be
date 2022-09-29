package com.example.week08.repository;

import com.example.week08.domain.MailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailAuthRepository extends JpaRepository<MailAuth,Long> {
    Optional<MailAuth> findByEmail(String email);
}
