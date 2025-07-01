package org.sopt.certi_server.domain.certificationcategory.repository;

import org.sopt.certi_server.domain.category.entity.Category;
import org.sopt.certi_server.domain.certificationcategory.entity.CertificationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationCategoryRepository extends JpaRepository<CertificationCategory, Long> {

    @Query("select cat from CertificationCategory cc join fetch cc.category cat where cc.certification.id = :certificationId")
    List<Category> getCategoriesByCertificationId(Long certificationId);
}
