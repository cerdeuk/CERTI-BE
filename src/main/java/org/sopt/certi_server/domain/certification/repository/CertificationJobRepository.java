package org.sopt.certi_server.domain.certification.repository;

import java.util.Optional;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationJob;
import org.sopt.certi_server.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationJobRepository extends JpaRepository<CertificationJob, Long> {

    @Query("select cj from CertificationJob cj join fetch cj.certification where cj.certification = :certification")
    List<CertificationJob> findAllByCertification(Certification certification);

    Optional<CertificationJob> findByCertificationAndJob(Certification certification, Job job);

    void deleteAllByCertification(Certification certification);

    @Query("""
        select cj
        from CertificationJob cj
        left join fetch cj.certification c
        join fetch c.tags
        where cj.job.name in :jobNames
""")
    List<CertificationJob> findByJobNames(List<String> jobNames);

    @Query("""
        select cj
        from CertificationJob cj
        left join fetch cj.certification c
        join fetch c.tags
        where cj.job.id in :jobIds
""")
    List<CertificationJob> findByJobIds(List<Long> jobIds);


}
