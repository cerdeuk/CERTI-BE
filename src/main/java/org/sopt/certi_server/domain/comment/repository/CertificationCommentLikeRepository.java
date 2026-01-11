package org.sopt.certi_server.domain.comment.repository;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.comment.entity.CertificationCommentLike;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CertificationCommentLikeRepository extends JpaRepository<CertificationCommentLike, Long> {


    /**
     * 좋아요 취소를 위해 '좋아요' 엔티티 조회
     */
    Optional<CertificationCommentLike> findByUserAndCertificationComment(User user, CertificationComment certificationComment);

    /**
     * 유저와 댓글에 해당하는 좋아요가 있는지 boolean값을 반환
     */
    boolean existsByUserAndCertificationComment(User user, CertificationComment certificationComment);


    /**
     * 부모 댓글이 삭제될 때, 참조하는 '좋아요' 기록을 모두 삭제 (FK 제약조건 위반 방지)
     */
    void deleteAllByCertificationComment(CertificationComment certificationComment);

    /**
     * 특정 사용자가 특정 자격증에 대한 댓글 중 좋아요를 단 댓글의 id 리스트를 반환
     */
    @Query("""
            SELECT ccl.certificationComment.id 
            FROM CertificationCommentLike ccl 
            WHERE ccl.user = :user AND ccl.certificationComment.certification = :certification
            """)
    List<Long> findLikedCommentIdsByCertificationAndUser(
            @Param("user") User user,
            @Param("certification") Certification certification
    );

    void deleteAllByUser(User user);
}
