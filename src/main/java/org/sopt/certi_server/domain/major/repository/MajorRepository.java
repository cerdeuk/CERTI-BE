package org.sopt.certi_server.domain.major.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.certi_server.domain.major.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
	Optional<Major> findByName(String name);
}
