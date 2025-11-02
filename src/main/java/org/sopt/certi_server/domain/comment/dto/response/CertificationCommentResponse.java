package org.sopt.certi_server.domain.comment.dto.response;

import org.sopt.certi_server.domain.comment.entity.CertificationComment;

import java.time.LocalDateTime;
import java.util.List;

public record CertificationCommentResponse(
    Long userId,
    String nickName,
    String content,
    String userMajor,
    List<String> userJob,
    String state,
    Long likeCount,
    LocalDateTime createdTime,
    LocalDateTime lastModifiedTime
) {
    public static CertificationCommentResponse from(
            CertificationComment certificationComment,
            String state,
            List<String> userJob
    ) {

        return new CertificationCommentResponse(
            certificationComment.getUser().getId(),
            certificationComment.getUser().getNickname(),
            certificationComment.getContent(),
            certificationComment.getUser().getMajor().getName(),
            userJob,
            state,
            certificationComment.getLikeCount(),
            certificationComment.getCreatedTime(),
            certificationComment.getLastModifiedTime()
        );
    }
}
