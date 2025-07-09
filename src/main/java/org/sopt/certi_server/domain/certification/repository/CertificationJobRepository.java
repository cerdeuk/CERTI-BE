package org.sopt.certi_server.domain.certification.repository;

import java.util.Optional;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationJob;
import org.sopt.certi_server.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationJobRepository extends JpaRepository<CertificationJob, Long> {
	Optional<CertificationJob> findByCertificationAndJob(Certification certification, Job job);
}
