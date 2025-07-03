package org.sopt.certi_server.domain.career.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.certi_server.domain.career.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {
	List<Career> findByUserId(Long userId);
	Optional<Career> findByUserIdAndId(Long userId, Long id);
}
