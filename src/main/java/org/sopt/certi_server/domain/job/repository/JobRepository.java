package org.sopt.certi_server.domain.job.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByName(String name);

    @Query("""
        select uj.job
        from UserJob uj
        where uj.user = :user
""")
    List<Job> findAllByUser(User user);
}
