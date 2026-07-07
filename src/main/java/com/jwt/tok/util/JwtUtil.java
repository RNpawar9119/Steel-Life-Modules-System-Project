package com.jwt.tok.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRATION = 10 * 60 * 60 * 1000; // 1 hour

    // ✅ Generate token with username + role + dealerCode
    public String generateToken(String username, String role, String dealerCode, String employeeCode) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("dealerCode", dealerCode)
                .claim("employeeCode", employeeCode)
                .claim("type", "WEB")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                // ✅ Java 8 + jjwt 0.9.1 signing style
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // ✅ Extract username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ✅ Extract role
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ✅ Extract dealerCode
    public String extractDealerCode(String token) {
        return extractAllClaims(token).get("dealerCode", String.class);
    }
    public String extractEmployeeCode(String token) {
        return extractAllClaims(token).get("employeeCode", String.class);
    }

    // ✅ Validate token
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return tokenUsername != null && tokenUsername.equals(username) && !isTokenExpired(token);
    }

    // 🔐 Helpers
    private Claims extractAllClaims(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // ✅ jjwt 0.9.1 parsing style (NO parserBuilder)
        return Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        Date exp = extractAllClaims(token).getExpiration();
        return exp != null && exp.before(new Date());
    }
}
