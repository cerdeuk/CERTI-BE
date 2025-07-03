package org.sopt.certi_server.domain.userprecertification.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserPreCertificationRepository extends JpaRepository<UserPreCertification, Long> {

    @Query("select upc from UserPreCertification upc join fetch upc.certification where upc.user.id = :userId")
    List<UserPreCertification> getPreCertificationsByUserId(Long userId);

    void deleteByUserAndCertification(User user, Certification certification);

}
