package org.example.quizapponlinebe.service;

import org.example.quizapponlinebe.dto.RegisterDto;
import org.example.quizapponlinebe.model.User;
import org.example.quizapponlinebe.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired IOtpTokenService otpTokenService;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void register(RegisterDto registerDto) {
        // Chuyển đổi RegisterDto sang User
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setRole(User.Role.USER);
        user.setProvider(User.Provider.LOCAL);
        user.setStatus(false);
        user.setIsDelete(false);
        user.setCreateAt(java.time.LocalDate.now());
        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
        otpTokenService.generateOtp(user,registerDto.getEmail());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }
}
