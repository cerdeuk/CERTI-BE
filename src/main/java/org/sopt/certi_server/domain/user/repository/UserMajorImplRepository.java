package org.sopt.certi_server.domain.user.repository;

import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserMajorImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMajorImplRepository extends JpaRepository<UserMajorImpl, Long> {
    UserMajorImpl findByUserId(Long userId);

	void deleteAllByUser(User user);
}
