package org.example.quizapponlinebe.service;

import org.example.quizapponlinebe.model.OtpToken;
import org.example.quizapponlinebe.model.User;
import org.example.quizapponlinebe.repository.IOtpTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
@Service
public class OtpTokenService implements IOtpTokenService{
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IOtpTokenRepository otpTokenRepository;

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP xác thc email");
        message.setText("OTP của bạn để xác minh email là: " + otp + "\nCó hiệu lực trong 5 phút.");
        message.setFrom(fromEmail);
        mailSender.send(message);
    }

    @Override
    public OtpToken generateOtp(User user, String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        OtpToken otpToken = new OtpToken();
        otpToken.setOtp(passwordEncoder.encode(otp));
        otpToken.setUser(user);
        otpToken.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpTokenRepository.save(otpToken);
        sendOtpEmail(email, otp);
        return otpToken;
    }
    @Override
    public boolean verifyOtp(int userId, String inputOtp) {
        // Tìm OTP token theo userId
        OtpToken otpToken = otpTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("OTP không tồn tại"));

        // Nếu OTP đã hết hạn
        if (otpToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpTokenRepository.delete(otpToken);
            return false;
        }

        // Nếu OTP đúng
        if (passwordEncoder.matches(inputOtp, otpToken.getOtp())) {
            otpTokenRepository.delete(otpToken); // Xác thực xong thì xóa
            return true;
        }

        // Nếu OTP sai
        otpToken.setAttempts(otpToken.getAttempts() + 1);

        if (otpToken.getAttempts() >= 3) {
            otpTokenRepository.delete(otpToken); // Quá 3 lần thì xoá luôn
        } else {
            otpTokenRepository.save(otpToken); // Cập nhật số lần thử
        }

        return false;
    }

}
