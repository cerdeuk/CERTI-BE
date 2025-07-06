package org.sopt.certi_server.domain.userpriorcertification.controller;

import java.util.List;

import org.sopt.certi_server.domain.userpriorcertification.dto.response.GetPriorCertificaitonResponse;
import org.sopt.certi_server.domain.userpriorcertification.dto.response.GetPriorCertificationDetailResponse;
import org.sopt.certi_server.domain.userpriorcertification.dto.response.GetPriorCertificationListResponse;
import org.sopt.certi_server.domain.userpriorcertification.service.UserPriorCertificationService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prior-certification")
public class UserPriorCertificationController {
	private final UserPriorCertificationService userPriorCertificationService;

	@PostMapping("/{certificationId}")
	public ResponseEntity<SuccessResponse<?>> addPriorCertification(
		@AuthenticationPrincipal Long userId,
		@PathVariable(name = "certificationId") Long certificationId
	){
		String certificationName = userPriorCertificationService.createPriorCertification(userId, certificationId);

		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE, certificationName));
	}

	@GetMapping("/{prior-certificationId}")
	public ResponseEntity<SuccessResponse<?>> getPriorCertification(
		@AuthenticationPrincipal Long userId,
		@PathVariable(name = "prior-certificationId") Long priorCertificationId
	){
		GetPriorCertificationDetailResponse getPriorCertificationDetailResponse = userPriorCertificationService.getPriorCertificationDetail(userId, priorCertificationId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getPriorCertificationDetailResponse));
	}

	@DeleteMapping("/{prior-certificationId}")
	public ResponseEntity<SuccessResponse<?>> deletePriorCertification(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long priorCertificationId
	){
		userPriorCertificationService.deletePriorCertification(userId, priorCertificationId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
	}

	@GetMapping
	public ResponseEntity<SuccessResponse<?>> getAllPriorCertifications(
		@AuthenticationPrincipal Long userId
	){
		List<GetPriorCertificaitonResponse> getPriorCertificaitonResponses = userPriorCertificationService. getPriorCertificaitonList(userId);
		GetPriorCertificationListResponse getPriorCertificationListResponse = GetPriorCertificationListResponse.of(getPriorCertificaitonResponses);

		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getPriorCertificationListResponse));
	}

}
