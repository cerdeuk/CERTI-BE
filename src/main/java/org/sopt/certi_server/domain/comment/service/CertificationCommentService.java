package org.sopt.certi_server.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.comment.dto.CommentRegisterRequest;
import org.sopt.certi_server.domain.comment.dto.CommentRegisterSuccessResponse;
import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.comment.repository.CertificationCommentRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CertificationCommentService {

    private final UserService userService;
    private final CertificationService certificationService;
    private final CertificationCommentRepository certificationCommentRepository;

    @Transactional
    public ResponseEntity<CommentRegisterSuccessResponse> registerComment(
            final Long userId,
            final Long certificationId,
            final CommentRegisterRequest request
    ){
        User user = userService.getUser(userId);
        Certification certification = certificationService.getCertification(certificationId);
        CertificationComment newCertificationComment = CertificationComment.builder()
                .user(user)
                .certification(certification)
                .content(request.content())
                .build();

        certificationCommentRepository.save(newCertificationComment);
    }
}
