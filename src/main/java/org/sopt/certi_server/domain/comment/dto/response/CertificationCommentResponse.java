package org.sopt.certi_server.domain.comment.dto.response;

import org.sopt.certi_server.domain.comment.entity.CertificationComment;

import java.time.LocalDateTime;
import java.util.List;

public record CertificationCommentResponse(
    Long userId,
    String nickName,
    String content,
    String userMajor,
    String userJob,
    String state,
    Long likeCount,
    LocalDateTime createdTime,
    LocalDateTime lastModifiedTime,
    boolean isLike
) {
    public static CertificationCommentResponse from(
            CertificationComment certificationComment,
            String state,
            String userJob,
            boolean isLike
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
            certificationComment.getLastModifiedTime(),
            isLike
        );
    }
}
