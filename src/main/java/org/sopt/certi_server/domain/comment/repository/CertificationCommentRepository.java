package org.sopt.certi_server.domain.comment.repository;


import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CertificationCommentRepository extends JpaRepository<CertificationComment, Long> {

    @Query(
            value = "SELECT cc FROM CertificationComment cc JOIN FETCH cc.user u where cc.certification.id = :certificationId",
            countQuery = "SELECT COUNT(c) FROM CertificationComment c WHERE c.certification.id = :certificationId"
    )
    Page<CertificationComment> findByCertificationId(
            @Param("certificationId") Long certificationId,
            Pageable pageable
    );

    /**
     * 동시성 문제를 해결하기 위한 원자적 카운트 증가
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE CertificationComment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :commentId")
    void incrementLikeCount(@Param("commentId") Long commentId);

    /**
     * 동시성 문제를 해결하기 위한 원자적 카운트 감소
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE CertificationComment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :commentId")
    void decrementLikeCount(@Param("commentId") Long commentId);

    void deleteAllByUser(User user);
}
