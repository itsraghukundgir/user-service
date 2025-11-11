package com.rentbike.userservice.service;

import com.rentbike.userservice.dto.LoginRequest;
import com.rentbike.userservice.dto.SignupRequest;
import com.rentbike.userservice.dto.UserResponse;
import com.rentbike.userservice.model.Role;
import com.rentbike.userservice.model.User;
import com.rentbike.userservice.repository.UserRepository;
import com.rentbike.userservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User register(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email Already in use");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone Already in use");
        }

        Role role = (request.getRole() != null) ? request.getRole() : Role.RENTER;

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .phoneNumber(request.getPhoneNumber())
                .build();

        return userRepository.save(user);
    }

    /**
     * Authenticate existing user and return JWT token with details.
     */
    public UserResponse authenticate(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return UserResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
