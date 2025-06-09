package org.example.quizapponlinebe.service;

import org.example.quizapponlinebe.dto.RegisterDto;
import org.example.quizapponlinebe.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void register(RegisterDto registerDto);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
}
