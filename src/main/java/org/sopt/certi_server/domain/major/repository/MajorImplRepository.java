package org.sopt.certi_server.domain.major.repository;

import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MajorImplRepository extends JpaRepository<MajorImpl, Long> {

    Optional<MajorImpl> findMajorImplByName(String name);

    List<MajorImpl> findByNameContainingIgnoreCase(String name);

    @Query("""
        select mi
        from MajorImpl mi
        join UserMajorImpl umi on umi.majorImpl.id = mi.id and umi.user = :user
""")
    List<MajorImpl> findMajorImplByUser(User user);
}
