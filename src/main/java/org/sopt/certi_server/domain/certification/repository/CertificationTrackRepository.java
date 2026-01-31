package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.entity.CertificationTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationTrackRepository extends JpaRepository<CertificationTrack, Long> {

}
