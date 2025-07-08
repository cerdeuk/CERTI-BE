package org.sopt.certi_server.domain.job.repository;

import java.util.Optional;

import org.sopt.certi_server.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
	Optional<Job> findByName(String name);
}
