package org.sopt.certi_server.domain.job.repository;

import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByName(String name);

    @Query("""
                    select j
                    from Job j
                    join CertificationJob cj on j.id = cj.job.id
                    where cj.certification.id = :certificationId
            """)
    List<Job> getJobsByCertificationId(Long certificationId);

    @Query("""
                    select uj.job
                    from UserJob uj
                    where uj.user = :user
            """)
    List<Job> findAllByUser(User user);
}
