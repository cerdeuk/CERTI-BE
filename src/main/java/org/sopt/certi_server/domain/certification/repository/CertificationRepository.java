package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.certification.entity.Category;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {

    @Query("select cat from CertificationCategory cc join cc.category cat where cc.certification.id = :certificationId")
    List<Category> getCategoriesByCertificationId(Long certificationId);

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
}
