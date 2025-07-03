package org.sopt.certi_server.domain.certification.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.dto.response.CertificationDetailResponse;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.favorite.service.FavoriteService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/certification")
public class CertificationController {

    private final CertificationService certificationService;
    private final FavoriteService favoriteService;

    @GetMapping(value = "/{certificationId}")
    public ResponseEntity<SuccessResponse<CertificationDetailResponse>> getCertification(@PathVariable Long certificationId){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, certificationService.getCertificationDetail(certificationId)));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> addCertification(@RequestBody CertificationCreateRequest request){
        certificationService.createCertification(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @PostMapping(value = "/{certificationId}/favorite")
    public ResponseEntity<SuccessResponse<Void>> toggleFavorite(
            @AuthenticationPrincipal @NotNull(message = "인증되지 않은 사용자입니다.") Long userId,
            @PathVariable Long certificationId
    ){
        boolean isCreated = favoriteService.toggleFavorite(userId, certificationId);
        if(isCreated){
            return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
        }else{
            return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
        }
    }
}
