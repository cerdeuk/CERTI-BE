package org.sopt.certi_server.domain.report.controller;

import org.sopt.certi_server.domain.report.service.ReportService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
@Tag(name = "Report 컨트롤러", description = "신고와 관련된 API를 처리합니다.")
public class ReportController {
	private final ReportService reportService;

	@PostMapping("/comment/{certification_comment_id}")
	@Operation(summary = "댓글 신고 API", description = "댓글을 신고합니다")
	public ResponseEntity<SuccessResponse<Void>> reportComment(
		@Parameter(
			name = "certification_comment_id",
			description = "신고할 댓글 ID",
			example = "1",
			required = true
		)
		@PathVariable(name = "certification_comment_id") Long commentId,
		@AuthenticationPrincipal Long userId
	){
		reportService.createCommentReport(userId, commentId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}
}
