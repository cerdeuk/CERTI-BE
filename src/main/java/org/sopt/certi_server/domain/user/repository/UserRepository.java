package org.sopt.certi_server.domain.user.repository;

import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname);
}
