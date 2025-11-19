package com.ifpb.sorrisus.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private final Key key;
    private final long jwtExpirationMs;

    public JWTUtil() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.jwtExpirationMs = 360000; 
    }

    public JWTUtil(@Value("${jwt.secret}") String jwtSecret,
                   @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
        if (jwtSecret == null || jwtSecret.trim().length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters");
        }
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        Object role = parseClaims(token).get("role");
        return role != null ? role.toString() : null;
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
