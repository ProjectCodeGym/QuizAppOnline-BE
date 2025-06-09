package org.example.quizapponlinebe.controller;

import org.example.quizapponlinebe.dto.ApiResponse;
import org.example.quizapponlinebe.dto.RegisterDto;
import org.example.quizapponlinebe.model.User;
import org.example.quizapponlinebe.repository.IUserRepository;
import org.example.quizapponlinebe.service.OtpTokenService;
import org.example.quizapponlinebe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private OtpTokenService otpTokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDto registerDto) {
        Errors errors = new BeanPropertyBindingResult(registerDto, "registerDto");
        registerDto.validate(registerDto, errors);
        if (errors.hasErrors()) {
            Map<String, String> validationErrors = new HashMap<>();
            errors.getFieldErrors().forEach(error -> {
                validationErrors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(new ApiResponse(false, HttpStatus.BAD_REQUEST.value(), "Dữ liệu không hợp lệ!", validationErrors));
        }
        if (userService.existsByUsername(registerDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, HttpStatus.CONFLICT.value(), "Tên đăng nhập đã được sử dụng!", null));
        }
        if (userService.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, HttpStatus.CONFLICT.value(), "Email đã được sử dụng!", null));
        }
        userService.register(registerDto);
        return ResponseEntity.ok(new ApiResponse(true, HttpStatus.OK.value(), "Vui lòng kiểm tra email để xác thực OTP!", null));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestParam int userId, @RequestParam String otp) {
        boolean verified = otpTokenService.verifyOtp(userId, otp);
        if (verified) {
            User user = userService.findById(userId).get();
            user.setStatus(true);
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse(true, HttpStatus.OK.value(), "Xác thực OTP thành công!", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(false, HttpStatus.BAD_REQUEST.value(), "Mã OTP không hợp lệ hoặc đã hết hạn!", null)
            );
        }
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestParam String email) {
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, HttpStatus.BAD_REQUEST.value(), "Email không tồn tại!", null));
        }

        User user = optionalUser.get();

        if (user.getStatus()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, HttpStatus.BAD_REQUEST.value(), "Tài khoản đã được xác thực!", null));
        }
        otpTokenService.generateOtp(user,user.getEmail());
        return ResponseEntity.ok(new ApiResponse(true, HttpStatus.OK.value(), "OTP mới đã được gửi về email!", null));
    }
}

