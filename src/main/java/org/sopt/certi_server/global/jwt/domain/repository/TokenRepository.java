package org.sopt.certi_server.global.jwt.domain.repository;

import java.util.Optional;

import org.sopt.certi_server.global.jwt.domain.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
	Optional<Token> findByRefreshToken(String refreshToken);
}
