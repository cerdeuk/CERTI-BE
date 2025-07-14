package org.sopt.certi_server.global.jwt.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtExtractor {

    private static final String BEARER = "Bearer ";

    private final SecretKey secretKey;

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("Jwt Token 파싱 실패: {}", e.getMessage());
            throw new UnauthorizedException();
        }
    }

    public String extractToken(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER)) {
            log.info("jwt 토큰 누락");
            throw new UnauthorizedException();
        }

        return authorization.substring(BEARER.length());
    }

    public Long extractUserId(String token) {
        Claims claims = extractClaims(token);
        try {
            return claims.get("userId", Long.class);
        } catch (IllegalArgumentException e) {
            log.error("토큰 유저 정보 누락: {}", e.getMessage());
            throw new UnauthorizedException();
        }
    }

}
