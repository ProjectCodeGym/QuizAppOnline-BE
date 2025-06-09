package org.example.quizapponlinebe.service;

import org.example.quizapponlinebe.model.OtpToken;
import org.example.quizapponlinebe.model.User;

import java.util.Optional;

public interface IOtpTokenService {
    OtpToken generateOtp(User user, String email);
    void sendOtpEmail(String to, String otp);
    boolean verifyOtp(int userId, String inputOtp);
}
