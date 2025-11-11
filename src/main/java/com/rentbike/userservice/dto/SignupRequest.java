package com.rentbike.userservice.dto;

import com.rentbike.userservice.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {

    @Email
    @NotBlank
    private String Email;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    @Pattern(regexp = "[0-9]{10,15}$", message = "Phone number must be 10–15 digits")
    private String phoneNumber;

    private Role role; // RENTER or SUPPLIER
}
