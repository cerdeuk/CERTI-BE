package org.sopt.certi_server.domain.userpriorcertification.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.userpriorcertification.entity.UserPriorCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPriorCertificationRepository extends JpaRepository<UserPriorCertification, Long> {
	Optional<UserPriorCertification> findByUserAndPriorCertificationId(User user, Long priorCertificationId);
	List<UserPriorCertification> findByUserOrderByPriorCertificationIdAsc(User user);
}
