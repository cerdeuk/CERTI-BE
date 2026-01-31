package org.sopt.certi_server.domain.comment.repository;

import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

public interface CertificationCommentRepositoryCustom {
    Page<CertificationComment> findByCertificationId(
            Long certificationId,
            Long userId,
            Pageable pageable
    );
}
