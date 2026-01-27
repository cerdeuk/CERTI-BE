package org.sopt.certi_server.global.jwt.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.sopt.certi_server.domain.user.dto.response.JwtResponse;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.BadRequestException;
import org.sopt.certi_server.global.jwt.core.JwtExtractor;
import org.sopt.certi_server.global.jwt.core.JwtProvider;
import org.sopt.certi_server.global.jwt.core.JwtValidator;
import org.sopt.certi_server.global.jwt.domain.entity.Token;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtProvider jwtProvider;
    private final JwtExtractor jwtExtractor;
    private final JwtValidator jwtValidator;
    private final TokenService tokenService;

    public JwtResponse issueToken(Long userId, String role) {
        String accessToken = jwtProvider.generateAccessToken(userId, role);
        String refreshToken = jwtProvider.generateRefreshToken(userId, role);
        return JwtResponse.of(accessToken, refreshToken);
    }

    public String generatePreSignupToken(String socialId){
        return jwtProvider.generatePreSignupToken(socialId);
    }

    /**
     * pre-signup token 유효성 검사
     */
    public void validatePreSignupToken(String authorizationHeader) {
        String token = jwtExtractor.extractToken(authorizationHeader);
        jwtValidator.validatePreSignupToken(token);
    }

    /**
     * refresh token 기반 재발급
     */
    public JwtResponse reissueToken(String authorizationHeader) {
        String refreshToken = jwtExtractor.extractToken(authorizationHeader);
        jwtValidator.validateRefreshToken(refreshToken); // 또는 refresh 전용 validator 추가 가능
        Long userId = jwtExtractor.extractUserId(refreshToken);
        String role = jwtExtractor.extractRole(authorizationHeader);

        Token findRefreshToken = tokenService.getTokenByUserId(userId);

        if(!findRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new BadRequestException(ErrorCode.MISMATCH_REFRESH_TOKEN);
        }
        log.info("Refresh 토큰 검증 성공");

        tokenService.deleteRefreshToken(userId);

        String newAccessToken = jwtProvider.generateAccessToken(userId, role);
        String newRefreshToken = jwtProvider.generateRefreshToken(userId, role);

        tokenService.saveRefreshToken(userId, newRefreshToken);

        return JwtResponse.of(newAccessToken, newRefreshToken);

    }


}
