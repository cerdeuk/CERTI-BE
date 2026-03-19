package org.sopt.certi_server.global.jwt.domain.service;

import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.sopt.certi_server.global.jwt.domain.entity.Token;
import org.sopt.certi_server.global.jwt.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

	private final TokenRepository tokenRepository;

	@Transactional
	public void saveRefreshToken(final Long userId, final String refreshToken) {
		log.info("Saving refresh token for userId : {}", userId);
		tokenRepository.save(Token.of(userId, refreshToken));
		log.info("Successfully Refresh token for userId : {}", userId);
	}

	public Long findIdByRefreshToken(final String refreshToken) {
		log.info("findIdByRefreshToken : {}", refreshToken);
		Token token = tokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
		return token.getId();
	}

	@Transactional
	public void deleteRefreshToken(final Long userId) {
		log.info("Deleting refresh token for userId : {}", userId);

		if (!tokenRepository.existsById(userId)) {
			log.info("{}의 Refresh token이 존재하지 않습니다", userId);
			return;
		}

		tokenRepository.deleteById(userId);
		log.info("Successfully deleted refresh token for userId : {}", userId);
	}

	public Token getTokenByUserId(final Long userId) {
		return tokenRepository.findById(userId)
			.orElseThrow(()-> new NotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
	}

	/*
		// 소셜 사용자 정보로 로그인/회원가입 분기 처리
    public SocialVerifyResult verifySocialUser(AuthProvider provider, SocialUserInfo userInfo) {
        Optional<UserIdentity> existingIdentity = userIdentityService
                .findByProviderAndProviderUserId(provider, userInfo.providerUserId());

        if (existingIdentity.isPresent()) {
            // 기존 회원 - userId 반환 (토큰 발급은 Facade에서)
            return SocialVerifyResult.registered(existingIdentity.get().getUserId());
        }

        // 신규 회원 - 임시 토큰 발급
        String tempToken = jwtProvider.createTempToken(provider, userInfo.providerUserId());
        return SocialVerifyResult.unregistered(tempToken);
    }

    // Temp Token 검증 및 정보 추출
    public TempTokenPayload verifyTempToken(String tempToken) {
        if (!jwtProvider.isTokenType(tempToken, TokenType.TEMP)) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }

        AuthProvider provider = jwtProvider.getProvider(tempToken);
        String providerUserId = jwtProvider.getProviderUserId(tempToken);

        return new TempTokenPayload(provider, providerUserId);
    }

    // Access / Refresh Token 발급
    @Transactional
    public AuthTokens issueTokens(Long userId, String role) {
        String accessToken = jwtProvider.createAccessToken(userId, role);
        String refreshToken = refreshTokenRepository.createToken();

        // Redis에 Refresh Token 저장 (token → userId)
        long ttlSeconds = jwtProvider.getRefreshTokenTtlSeconds();
        refreshTokenRepository.save(refreshToken, userId, ttlSeconds);

        return AuthTokens.of(accessToken, refreshToken, userId);
    }

    // Refresh Token 검증 및 Rotation (원자적 상태 변경)
    @Transactional
    public Long validateAndRotateToken(String refreshToken) {
        // 원자적으로 VALID → USED 변경 시도 (변경 전 상태 반환)
        RefreshTokenValue tokenValue = refreshTokenRepository.markAsUsedIfValid(refreshToken)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // 원본 상태 확인 (VALID였으면 이미 USED로 변경됨)
        switch (tokenValue.status()) {
            case USED -> {
                log.warn("토큰 탈취 감지 userId: {}", tokenValue.userId());
                refreshTokenRepository.revokeAllByUserId(tokenValue.userId());
                throw new AuthException(AuthErrorCode.REFRESH_TOKEN_REUSED);
            }
            case REVOKED -> throw new AuthException(AuthErrorCode.REFRESH_TOKEN_REVOKED);
            case VALID -> {} // 정상 - 이미 USED로 변경됨
        }

        if (tokenValue.isExpired()) {
            throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);
        }

        return tokenValue.userId();
    }

    // 로그아웃 (Blacklist + RTR)
    @Transactional
    public void logout(String accessToken, String refreshToken) {
        if (accessToken != null) {
            long remainingTtl = jwtProvider.getRemainingTtlSeconds(accessToken);
            if (remainingTtl > 0) {
                accessTokenBlacklist.blacklist(accessToken, remainingTtl);
            }
        }

        // Refresh Token REVOKED로 변경 및 삭제
        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken)
                    .ifPresent(value -> {
                        refreshTokenRepository.updateStatus(refreshToken, RefreshTokenStatus.REVOKED);
                        refreshTokenRepository.delete(refreshToken, value.userId());
                    });
        }
    }

    // 전체 로그아웃 (모든 Refresh Token 무효화)
    @Transactional
    public void withdraw(Long userId, String accessToken) {
        // Access Token Blacklist 추가
        if (accessToken != null) {
            long remainingTtl = jwtProvider.getRemainingTtlSeconds(accessToken);
            if (remainingTtl > 0) {
                accessTokenBlacklist.blacklist(accessToken, remainingTtl);
            }
        }

        // 모든 Refresh Token 삭제
        refreshTokenRepository.deleteAllByUserId(userId);
		userIdentityRepository.deleteAllByUserId(userId);
    }

	 */
}
