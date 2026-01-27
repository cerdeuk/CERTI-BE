package org.sopt.certi_server.global.jwt.core;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.global.jwt.config.JwtProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private static final String USER_ID = "userId";
    private static final String ROLE = "role";
    private static final String SOCIAL_ID = "socialId";

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public String generateAccessToken(Long userId, String role) {
        return generateToken(Map.of(USER_ID, userId, ROLE, role), jwtProperties.getAccessTokenExpirationTime());
    }

    public String generateRefreshToken(Long userId, String role) {
        return generateToken(Map.of(USER_ID, userId, ROLE, role), jwtProperties.getRefreshTokenExpirationTime());
    }

    public String generatePreSignupToken(String socialId){
        return generateToken(Map.of(SOCIAL_ID, socialId), jwtProperties.getPreSignupTokenExpirationTime());
    }

    public String generateToken(Map<String, Object> claims, long expirationTime) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }
}
