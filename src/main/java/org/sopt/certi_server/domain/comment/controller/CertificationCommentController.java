package org.sopt.certi_server.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.comment.dto.request.CommentRegisterRequest;
import org.sopt.certi_server.domain.comment.dto.response.CertificationCommentResponse;
import org.sopt.certi_server.domain.comment.service.CertificationCommentService;
import org.sopt.certi_server.global.error.code.SuccessCode;
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
public class CertificationCommentController {

    private final CertificationCommentService certificationCommentService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> registerCertificationComment(
            @RequestBody CommentRegisterRequest commentRegisterRequest,
            @AuthenticationPrincipal Long userId
    ){
        certificationCommentService.registerComment(userId, commentRegisterRequest);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<SuccessResponse<Void>> deleteCertificationComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable(value = "commentId") Long commentId
    ){
        certificationCommentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<Page<CertificationCommentResponse>>> getCommentList(
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

        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, responsePage));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<SuccessResponse<Void>> toggleCommentLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable(value = "commentId") Long commentId
    ){
        certificationCommentService.toggleCommentLike(userId, commentId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }
}
