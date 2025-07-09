package org.sopt.certi_server.domain.admin.controller;

import org.sopt.certi_server.domain.admin.dto.request.CreateJobRequest;
import org.sopt.certi_server.domain.admin.dto.request.CreateMajorRequest;
import org.sopt.certi_server.domain.admin.service.AdminService;
import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/{certificationId}/major")
	public ResponseEntity<SuccessResponse> addMajor(
		@PathVariable Long certificationId,
		@RequestBody CreateMajorRequest request){
		adminService.createMajor(certificationId, request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@DeleteMapping("/{certificationId}/major")
	public ResponseEntity<SuccessResponse> deleteMajor(
		@PathVariable Long certificationId,
		@RequestBody String majorName
	){
		adminService.deleteMajor(certificationId, majorName);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
	}

	@PostMapping("/{certificationId}/job")
	public ResponseEntity<SuccessResponse> addJob(
		@PathVariable Long certificationId,
		@RequestBody CreateJobRequest createJobRequest
	){
		adminService.createJob(certificationId, createJobRequest);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@DeleteMapping("/{certificationId}/job")
	public ResponseEntity<SuccessResponse> deleteJob(
		@PathVariable Long certificationId,
		@RequestBody String jobName
	){
		adminService.deleteJob(certificationId, jobName);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
	}
}
