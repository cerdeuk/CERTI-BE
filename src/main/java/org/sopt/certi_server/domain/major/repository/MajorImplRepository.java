package org.sopt.certi_server.domain.major.repository;

import java.util.List;

import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MajorImplRepository extends JpaRepository<MajorImpl, Long> {

    Optional<MajorImpl> findMajorImplByName(String name);
	List<MajorImpl> findByNameContainingIgnoreCase(String name);
}
