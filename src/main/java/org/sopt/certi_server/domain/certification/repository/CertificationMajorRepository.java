package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationMajor;
import org.sopt.certi_server.domain.major.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationMajorRepository extends JpaRepository<CertificationMajor, Long> {

    @Query("select cm from CertificationMajor cm join fetch cm.major where cm.certification = :certification")
    List<CertificationMajor> findAllByCertification(Certification certification);


    Optional<CertificationMajor> findByCertificationAndMajor(Certification certification, Major major);

    void deleteAllByCertification(Certification certification);
}
