package org.sopt.certi_server.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.certi_server.domain.user.entity.Career;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {
	@Query("SELECT ce FROM Career ce WHERE ce.user.id = :userId ORDER BY ce.endAt DESC")
	List<Career> findByUserId(@Param("userId") Long userId);

	@Query("SELECT ce FROM Career ce WHERE ce.user.id = :userId AND ce.id = :id")
	Optional<Career> findByUserIdAndId(Long userId, Long id);

	int countByUser(User user);
}
