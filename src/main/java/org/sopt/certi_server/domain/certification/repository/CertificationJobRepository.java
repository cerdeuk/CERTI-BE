package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationJobRepository extends JpaRepository<CertificationJob, Long> {

    @Query("select cj from CertificationJob cj join fetch cj.certification where cj.certification = :certification")
    List<CertificationJob> findAllByCertification(Certification certification);

    void deleteAllByCertification(Certification certification);
}
