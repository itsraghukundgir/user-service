package com.rentbike.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for creating and validating JWT tokens.
 */
@Component
public class JwtUtil {
    private final Key key;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
    @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
        // secret must be at least 32 bytes for HS256 algorithm
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    /**
     * Generate JWT token for a user.
     *
     * @param subject email (used as JWT subject)
     * @param role user role (RENTER / SUPPLIER)
     * @return JWT token string
     */

    public String generateToken(String subject, String role) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate and parse JWT token.
     * @param token JWT string
     * @return Parsed JWS claims
     * @throws JwtException if invalid or expired
     */
    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    /**
     * Extract user email (subject) from token.
     */
    public String getEmailFromToken(String token) {
        return validateToken(token).getBody().getSubject();
    }


    /**
     * Extract role claim from token.
     */
    public String getRoleFromToken(String token) {
        return validateToken(token).getBody().get("role", String.class);
    }
}
