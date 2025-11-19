package com.stock.service;


import com.stock.SecurityConfig.JwtUtil;
import com.stock.dto.AuthRequest;
import com.stock.dto.AuthResponse;
import com.stock.dto.RegisterRequest;
import com.stock.entity.User;
import com.stock.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String register(RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            return "User already exists!";
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));  // hashing
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setCreatedDate(LocalDateTime.now());
        user.setNumberOfKitta(0);
        user.setRoles("ROLE_USER");

        userRepository.save(user);
        return "User registered successfully!";
    }

    public AuthResponse login(AuthRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    public void clearExpiredForgetCodes() {
        userRepository.findAll().forEach(u -> {
            if (u.getForgetPasswordCodeExpiry() != null &&
                    u.getForgetPasswordCodeExpiry().isBefore(LocalDateTime.now())) {
                u.setForgetPasswordCode(null);
                u.setForgetPasswordCodeExpiry(null);
                userRepository.save(u);
            }
        });
    }
}

