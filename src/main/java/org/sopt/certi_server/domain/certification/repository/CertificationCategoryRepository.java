package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationCategoryRepository extends JpaRepository<CertificationCategory, Long> {
    void deleteAllByCertification(Certification certification);
}
