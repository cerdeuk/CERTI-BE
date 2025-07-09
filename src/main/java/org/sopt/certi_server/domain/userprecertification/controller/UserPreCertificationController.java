package org.sopt.certi_server.domain.userprecertification.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.userprecertification.dto.request.UserPreCertificationRequest;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimpleListResponse;
import org.sopt.certi_server.domain.userprecertification.service.UserPreCertificationService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/home/pre-certification")
@RequiredArgsConstructor
public class UserPreCertificationController {

     private final UserPreCertificationService userPreCertificationService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PreCertificationSimpleListResponse>> getPreCertificationListData(
            @AuthenticationPrincipal @NotNull(message = "인증되지 않은 사용자입니다.") Long userId
    ){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, userPreCertificationService.getPreCertificationListDataByUserId(userId)));
    }

    @PostMapping("/{certificationId}")
    public ResponseEntity<SuccessResponse<Void>> addPreCertification(
            @AuthenticationPrincipal @NotNull(message = "인증되지 않은 사용자입니다.") Long userId,
            @PathVariable(name = "certificationId") Long certificationId
    ){
        userPreCertificationService.createNewPreCertification(userId, certificationId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @DeleteMapping(value = "/{certificationId}")
    public ResponseEntity<SuccessResponse<Void>> deletePreCertification(
            @AuthenticationPrincipal @NotNull(message = "인증되지 않은 사용자입니다.") Long userId,
            @PathVariable Long certificationId
    ){
        userPreCertificationService.deletePreCertification(userId, certificationId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
    }
}
