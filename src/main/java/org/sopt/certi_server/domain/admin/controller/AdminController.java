package org.sopt.certi_server.domain.admin.controller;

import org.sopt.certi_server.domain.admin.dto.request.*;
import org.sopt.certi_server.domain.admin.dto.response.AdminCertificationDetailResponse;
import org.sopt.certi_server.domain.admin.dto.response.AdminCertificationListResponse;
import org.sopt.certi_server.domain.admin.service.AdminService;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
	private final AdminService adminService;
    private final CertificationService certificationService;

    @PostMapping(value = "/certification")
    public ResponseEntity<SuccessResponse<Void>> addCertification(@RequestBody CertificationCreateRequest request){
        certificationService.createCertification(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

	@PostMapping(value = "/major")
	public ResponseEntity<SuccessResponse<Void>> addMajor(@RequestBody MajorCreateRequest request){
		adminService.addMajor(request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@PostMapping(value = "/major-impl")
	public ResponseEntity<SuccessResponse<Void>> addMajorImpl(@RequestBody MajorImplCreateRequest request){
		adminService.addMajorImpl(request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@PostMapping(value = "/certification-major")
	public ResponseEntity<SuccessResponse<Void>> addCertificationMajor(@RequestBody CertificationMajorCreateRequest request){
		adminService.addCertificationMajor(request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@PostMapping(value = "/job")
	public ResponseEntity<SuccessResponse<Void>> addJob(@RequestBody JobCreateRequest request){
		adminService.addJob(request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@PostMapping(value = "/certification-job")
	public ResponseEntity<SuccessResponse<Void>> addCertificationJob(@RequestBody CertificationJobCreateRequest request){
		adminService.addCertificationJob(request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@PostMapping(value = "/agency")
	public ResponseEntity<SuccessResponse<Void>> addAgency(@RequestBody AgencyCreateRequest request){
		adminService.addAgency(request);
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
