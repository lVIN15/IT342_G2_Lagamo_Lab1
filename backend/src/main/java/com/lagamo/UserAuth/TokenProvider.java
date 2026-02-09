package com.lagamo.UserAuth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {

    // Reads the secret key from application.properties
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // Token validity: 24 hours (in milliseconds)
    private final long jwtExpirationMs = 86400000; 

    private Key getSigningKey() {
        // Use the secret key to sign the token
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // 1. Generate Token (from Class Diagram)
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Store email inside token
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 2. Validate Token (from Class Diagram)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT Token: " + e.getMessage());
        }
        return false;
    }

    // 3. Get Email from Token (Utility)
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}