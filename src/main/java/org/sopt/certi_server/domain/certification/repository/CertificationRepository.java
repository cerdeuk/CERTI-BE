package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long>, CertificationRepositoryCustom {

    @Query("select j from CertificationJob cj join cj.job j where cj.certification.id = :certificationId")
    List<Job> getJobsByCertificationId(Long certificationId);

    @Query("""
       select new org.sopt.certi_server.domain.certification.dto.response.CertificationSimple(
        c,
        case when f is not null then true else false END
       ) 
       from Certification c 
       left join Favorite f on f.certification = c and f.user = :user 
       where c.name like concat('%', :keyword, '%') 
""")
    List<CertificationSimple> searchByKeyword(User user, String keyword);

    Optional<Certification> findByName(String certificationName);
}
