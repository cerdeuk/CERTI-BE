package org.sopt.certi_server.domain.comment.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CommentRegisterRequest(
        @NotEmpty(message = "댓글의 내용은 필수입니다.") @Size(message = "댓글의 크기는 100자를 넘을 수 없습니다.", max = 100) String content,
        Long certificationId
) {
}
