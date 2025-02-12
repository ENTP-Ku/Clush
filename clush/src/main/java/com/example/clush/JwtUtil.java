package com.example.clush;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "secretKey"; // 🔹 비밀 키
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 🔹 토큰 유효 시간 (2시간)

    // 🔹 JWT 생성 메서드
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(userId.toString()) // 사용자 ID를 Subject로 설정
                .claim("username", username) // username 정보 추가
                .setIssuedAt(new Date()) // 발급 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 서명
                .compact();
    }

    // 🔹 JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    // 🔹 JWT에서 사용자 ID 추출
    public Long extractUserId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    // 🔹 JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }

    // 🔹 JWT 파싱하여 Claims 반환
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
