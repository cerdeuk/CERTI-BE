package org.sopt.certi_server.domain.user.repository;

import org.sopt.certi_server.domain.user.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {
    List<University> findAllByNameContaining(String name);

    Optional<University> findByName(String name);
}
