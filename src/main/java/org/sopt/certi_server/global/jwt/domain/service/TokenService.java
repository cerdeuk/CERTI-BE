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
		Token token = tokenRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
		tokenRepository.delete(token);
		log.info("Successfully deleted refresh token for userId : {}", userId);
	}

	public Token getTokenByUserId(final Long userId) {
		return tokenRepository.findById(userId)
			.orElseThrow(()-> new NotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
	}
}
