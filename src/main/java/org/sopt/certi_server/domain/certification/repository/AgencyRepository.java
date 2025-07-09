package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

    Optional<Agency> findByName(String agencyName);
}
