package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationMajorRepository extends JpaRepository<CertificationMajor, Long> {

    @Query("select cm from CertificationMajor cm join fetch cm.major where cm.certification = :certification")
    List<CertificationMajor> findAllByCertification(Certification certification);

    void deleteAllByCertification(Certification certification);
}
