package com.rentbike.userservice.controller;

import com.rentbike.userservice.dto.LoginRequest;
import com.rentbike.userservice.dto.SignupRequest;
import com.rentbike.userservice.dto.UserResponse;
import com.rentbike.userservice.model.User;
import com.rentbike.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle user-related endpoints:
 * - POST /api/users/signup
 * - POST /api/users/login
 * - (Future) GET /api/users/me
 */

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Register a new user (RENTER or SUPPLIER)
     */

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        User created = userService.register(request);

        UserResponse userResponse = UserResponse.builder()
                .userId(created.getId())
                .email(created.getEmail())
                .name(created.getName())
                .phoneNumber(created.getPhoneNumber())
                .role(created.getRole().name())
                .build();

        return ResponseEntity.ok(userResponse);
    }

    /**
     * Login existing user and return JWT + user details.
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest req) {
        UserResponse resp = userService.authenticate(req);
        return ResponseEntity.ok(resp);
    }
}
