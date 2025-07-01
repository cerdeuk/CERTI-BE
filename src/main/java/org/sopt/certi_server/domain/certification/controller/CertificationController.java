package org.sopt.certi_server.domain.certification.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.dto.response.CertificationDetailResponse;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/certification")
public class CertificationController {

    private final CertificationService certificationService;

    @GetMapping(value = "/{certificationId}")
    public ResponseEntity<SuccessResponse<CertificationDetailResponse>> getCertification(Long certificationId){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, certificationService.getCertificationDetail(certificationId)));
    }
}
