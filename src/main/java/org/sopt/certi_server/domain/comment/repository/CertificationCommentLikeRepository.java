package org.sopt.certi_server.domain.comment.repository;

import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.comment.entity.CertificationCommentLike;
import org.sopt.certi_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationCommentLikeRepository extends JpaRepository<CertificationCommentLike, Long> {


    /**
     * 좋아요 취소를 위해 '좋아요' 엔티티 조회
     */
    Optional<CertificationCommentLike> findByUserAndCertificationComment(User user, CertificationComment certificationComment);

    /**
     * 부모 댓글이 삭제될 때, 참조하는 '좋아요' 기록을 모두 삭제 (FK 제약조건 위반 방지)
     */
    void deleteAllByCertificationComment(CertificationComment certificationComment);
}
