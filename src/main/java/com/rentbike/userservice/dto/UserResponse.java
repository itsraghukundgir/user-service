package com.rentbike.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String token;        // present only after login
    private Long userId;
    private String email;
    private String name;
    private String role;
    private String phoneNumber;
}
