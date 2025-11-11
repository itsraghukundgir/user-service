package com.rentbike.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Basic security configuration to provide PasswordEncoder bean.
 * This allows UserService to inject the encoder.
 */

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disable CSRF for testing APIs (enable later when you configure cookies correctly)
                .csrf(csrf -> csrf.disable())

                // stateless session since we'll use JWT later; for now it prevents session login behavior
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // configure endpoints
                .authorizeHttpRequests(auth -> auth
                        // allow signup & login publicly
                        .requestMatchers("/api/users/signup", "/api/users/login", "/h2-console/**", "/actuator/**").permitAll()
                        // anything else must be authenticated
                        .anyRequest().authenticated()
                )

                // disable form login and basic auth (so we don't get the Basic 401)
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

        // allow frames for H2 console in dev (if you use it)
        ;

        // If you later add a JwtAuthenticationFilter, add it here before UsernamePasswordAuthenticationFilter:
        // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
