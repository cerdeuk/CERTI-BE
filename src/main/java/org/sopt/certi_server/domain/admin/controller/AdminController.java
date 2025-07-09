package org.sopt.certi_server.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.admin.dto.response.AdminCertificationDetailResponse;
import org.sopt.certi_server.domain.admin.dto.response.AdminCertificationListResponse;
import org.sopt.certi_server.domain.admin.service.AdminService;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

    private final CertificationService certificationService;
    private final AdminService adminService;

    @PostMapping(value = "/certification")
    public ResponseEntity<SuccessResponse<Void>> addCertification(@RequestBody CertificationCreateRequest request){
        certificationService.createCertification(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @GetMapping(value = "/certification/{certificationId}")
    public ResponseEntity<SuccessResponse<AdminCertificationDetailResponse>> getCertificationDetail(@PathVariable Long certificationId){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, adminService.getCertificationDetail(certificationId)));
    }

    @GetMapping(value = "/certification")
    public ResponseEntity<SuccessResponse<AdminCertificationListResponse>> getAllCertifications(){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, adminService.getAllCertifications()));
    }

    @DeleteMapping(value = "/certification/{certificationId}")
    public ResponseEntity<SuccessResponse<Void>> deleteCertification(@PathVariable Long certificationId){
        adminService.deleteCertification(certificationId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
    }

}
