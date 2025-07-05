package org.sopt.certi_server.global.jwt.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sopt.certi_server.global.jwt.domain.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
class TokenRepositoryTest {
	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@DisplayName("Token 객체를 Redis에 저장하고 조회할 수 있다")
	@Test
	void saveAndFindByRefreshToken() {
	    //given
	    Long userId = 1L;
		String refreshToken = "test-refresh-token";

		Token token = Token.of(userId, refreshToken);

	    //when
	    tokenRepository.save(token);

	    //then
		Optional<Token> savedToken = tokenRepository.findById(userId);
		assertThat(savedToken).isPresent();
		assertThat(savedToken.get().getRefreshToken()).isEqualTo(refreshToken);

		Optional<Token> byRefreshToken = tokenRepository.findByRefreshToken(refreshToken);
		assertThat(byRefreshToken).isPresent();
		assertThat(byRefreshToken.get().getId()).isEqualTo(userId);

	 }

	 /*
	@Test
	@DisplayName("Token 객체를 Redis에서 삭제할 수 있다")
	void deleteToken() {
		// given
		Long userId = 2L;
		String refreshToken = "delete-refresh-token";
		Token token = Token.of(userId, refreshToken);
		tokenRepository.save(token);

		// when
		tokenRepository.deleteById(userId);

		// then
		Optional<Token> deletedToken = tokenRepository.findById(userId);
		assertThat(deletedToken).isNotPresent();
	}
*/
}
