package org.sopt.certi_server.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.admin.dto.request.*;
import org.sopt.certi_server.domain.admin.dto.response.AdminCertificationDetailResponse;
import org.sopt.certi_server.domain.admin.dto.response.AdminCertificationListResponse;
import org.sopt.certi_server.domain.admin.dto.response.AdminCertificationResponse;
import org.sopt.certi_server.domain.admin.service.AdminService;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.PageResponse;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "관리자 전용 API", description = "관리자 전용 자격증 API 입니다.")
public class AdminController {
    private final AdminService adminService;
    private final CertificationService certificationService;

    @PostMapping(value = "/certification")
    @Operation(summary = "자격증 추가 API", description = "자격증을 생성하고 저장합니다.")
    public ResponseEntity<SuccessResponse<Void>> addCertification(@RequestBody CertificationCreateRequest request) {
        certificationService.createCertification(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @PostMapping(value = "/major")
    @Operation(summary = "전공(대분류) 추가 API", description = "전공(대분류) 정보를 추가합니다.")
    public ResponseEntity<SuccessResponse<Void>> addMajor(@RequestBody MajorCreateRequest request) {
        adminService.addMajor(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }


    @PostMapping(value = "/major-impl")
    @Operation(summary = "전공(소분류) 추가 API", description = "전공(소분류) 정보를 추가합니다.")
    public ResponseEntity<SuccessResponse<Void>> addMajorImpl(@RequestBody MajorImplCreateRequest request) {
        adminService.addMajorImpl(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @PostMapping(value = "/certification-major")
    @Operation(summary = "자격증 - 전공(대분류) 매핑 정보 추가 API", description = "자격증과 전공(대분류) 매핑 정보(가중치)를 추가합니다.")
    public ResponseEntity<SuccessResponse<Void>> addCertificationMajor(@RequestBody CertificationMajorCreateRequest request) {
        adminService.addCertificationMajor(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @PatchMapping(value = "/certification-major")
    @Operation(summary = "자격증 - 전공(대분류) 매핑 정보 수정 API", description = "자격증과 전공(대분류) 매핑 정보(가중치)를 수정합니다.")
    public ResponseEntity<SuccessResponse<Void>> patchCertificationMajor(
            @RequestBody CertificationMajorPatchRequest request
    ){
        adminService.updateCertificationMajor(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }

    @PostMapping(value = "/job")
    @Operation(summary = "직무 정보 추가 API", description = "직무 정보를 추가합니다.")
    public ResponseEntity<SuccessResponse<Void>> addJob(@RequestBody JobCreateRequest request) {
        adminService.addJob(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @PostMapping(value = "/certification-job")
    @Operation(summary = "자격증 - 직무 매핑 정보 추가 API", description = "자격증과 직무 매핑 정보(가중치)를 추가합니다.")
    public ResponseEntity<SuccessResponse<Void>> addCertificationJob(@RequestBody CertificationJobCreateRequest request) {
        adminService.addCertificationJob(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @PatchMapping(value = "/certification-job")
    @Operation(summary = "자격증 - 직무 매핑 정보 수정 API", description = "자격증과 직무 매핑 정보(가중치)를 수정합니다.")
    public ResponseEntity<SuccessResponse<Void>> patchCertificationJob(@RequestBody CertificationJobPatchRequest request){
        adminService.updateCertificationJob(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }

    @PostMapping(value = "/agency")
    @Operation(summary = "인증기관 추가 API", description = "인증기관을 추가합니다.")

    public ResponseEntity<SuccessResponse<Void>> addAgency(@RequestBody AgencyCreateRequest request) {
        adminService.addAgency(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));

    }

    @GetMapping(value = "/certification/{certificationId}")
    @Operation(summary = "자격증 상세 조회 API", description = "자격증을 상세조회하는 API입니다.")
    public ResponseEntity<SuccessResponse<AdminCertificationDetailResponse>> getCertificationDetail(@PathVariable Long certificationId) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, adminService.getCertificationDetail(certificationId)));
    }

    @GetMapping(value = "/certification")
    @Operation(summary = "자격증 리스트 조회 API", description = "자격증 리스트를 페이징을 통해 조회합니다.")
    public ResponseEntity<SuccessResponse<PageResponse<AdminCertificationResponse>>> getAllCertifications(
            @Valid @RequestBody PageRequest request
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, adminService.getAllCertifications(request)));
    }

    @DeleteMapping(value = "/certification/{certificationId}")
    @Operation(summary = "", description = "")
    public ResponseEntity<SuccessResponse<Void>> deleteCertification(@PathVariable Long certificationId) {
        adminService.deleteCertification(certificationId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
    }


}
