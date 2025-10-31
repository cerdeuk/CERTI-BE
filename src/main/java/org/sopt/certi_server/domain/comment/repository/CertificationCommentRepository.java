package org.sopt.certi_server.domain.comment.repository;

import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationCommentRepository extends JpaRepository<CertificationComment, Long> {

}
