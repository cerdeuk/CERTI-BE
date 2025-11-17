package org.sopt.certi_server.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.comment.dto.request.CommentRegisterRequest;
import org.sopt.certi_server.domain.comment.dto.response.CertificationCommentResponse;
import org.sopt.certi_server.domain.comment.service.CertificationCommentService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.PageResponse;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/comments")
@Tag(name = "CertificationComment 컨트롤러", description = "자격증 댓글과 관련된 API를 처리합니다.")
public class CertificationCommentController {

    private final CertificationCommentService certificationCommentService;

    @PostMapping
    @Operation(summary = "댓글 등록 API", description = "댓글을 등록합니다.")
    public ResponseEntity<SuccessResponse<Void>> registerCertificationComment(
            @RequestBody CommentRegisterRequest commentRegisterRequest,
            @AuthenticationPrincipal Long userId
    ){
        certificationCommentService.registerComment(userId, commentRegisterRequest);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @DeleteMapping(value = "/{commentId}")
    @Operation(summary = "댓글 삭제 API", description = "댓글을 삭제합니다.")
    public ResponseEntity<SuccessResponse<Void>> deleteCertificationComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable(value = "commentId") Long commentId
    ){
        certificationCommentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
    }

    @GetMapping
    @Operation(summary = "댓글 조회 API", description = "해당 자격증의 댓글을 조회합니다.")
    public ResponseEntity<SuccessResponse<PageResponse<CertificationCommentResponse>>> getCommentList(
        @AuthenticationPrincipal Long userId,
        @RequestParam(value = "certificationId") Long certificationId,
        @PageableDefault(
                page = 0,
                size = 10,
                sort = "id",
                direction = Sort.Direction.DESC
        ) final Pageable pageable
    ){
        Page<CertificationCommentResponse> responsePage = certificationCommentService.getCommentsByCertification(userId, certificationId, pageable);

        PageResponse<CertificationCommentResponse> responsePageDto = PageResponse.from(responsePage);

        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, responsePageDto));
    }

    @PostMapping("/{commentId}/like")
    @Operation(summary = "댓글 좋아요/좋아요 취소 API", description = "댓글을 좋아요/좋아요 취소합니다.")
    public ResponseEntity<SuccessResponse<Void>> toggleCommentLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable(value = "commentId") Long commentId
    ){
        certificationCommentService.toggleCommentLike(userId, commentId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }
}
