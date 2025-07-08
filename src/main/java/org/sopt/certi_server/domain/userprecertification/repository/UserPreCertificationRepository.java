package org.sopt.certi_server.domain.userprecertification.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPreCertificationRepository extends JpaRepository<UserPreCertification, Long> {


    // 원래 fetch join의 대상에는 별칭을 지정해주어선 안되지만, 체인 형식으로 쓰는 경우에는 사용할 수 있도록 함
    @Query("""
            select upc 
            from UserPreCertification upc 
            join fetch upc.certification c 
            left join fetch c.agency
            where upc.user.id = :userId
            """)
    List<UserPreCertification> getPreCertificationsByUserId(Long userId);

    Optional<UserPreCertification> findFirstByUserOrderByCreatedTimeDesc(User user);

    void deleteByUserAndCertification(User user, Certification certification);

}
