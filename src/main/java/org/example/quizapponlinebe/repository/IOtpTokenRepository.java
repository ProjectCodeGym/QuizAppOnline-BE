package org.example.quizapponlinebe.repository;

import org.example.quizapponlinebe.model.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOtpTokenRepository extends JpaRepository<OtpToken,Integer> {
    Optional<OtpToken> findByUserId(int id);
}
