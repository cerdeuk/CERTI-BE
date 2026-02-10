package org.sopt.certi_server.domain.report.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 신고 요청 DTO")
public record CommentReportRequest(

	@Schema(
		description = "신고 사유",
		example = "욕설이 포함되어 있습니다."
	)
	@Size(max = 100, message = "신고 내용은 최소 1자, 최대 100자입니다")
	@Nullable
	String content,

	@Schema(
		description = "신고와 함께 댓글 작성자 차단 요청 여부",
		example = "true"
	)
	@NotNull(message = "사용자 차단여부는 필수입니다")
	boolean shouldBlockUser
) {
}
