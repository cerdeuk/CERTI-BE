package org.sopt.certi_server.domain.acquisition.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionResponse;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionDetailResponse;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionListResponse;
import org.sopt.certi_server.domain.acquisition.service.AcquisitionService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/acquisition")
public class AcquisitionController {
	private final AcquisitionService acquisitionService;

	@PostMapping("/{certificationId}")
	public ResponseEntity<SuccessResponse<?>> addAcquisition(
		@AuthenticationPrincipal Long userId,
		@PathVariable(name = "certificationId") Long certificationId
	){
		boolean isAcquired = acquisitionService.createAcquisition(userId, certificationId);

		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE, isAcquired));
	}

	@GetMapping("/{acquisitionId}")
	public ResponseEntity<SuccessResponse<GetAcquisitionDetailResponse>> getAcquisition(
		@AuthenticationPrincipal Long userId,
		@PathVariable(name = "acquisitionId") Long acquisitionId
	){
		GetAcquisitionDetailResponse getAcquisitionDetailResponse = acquisitionService.getAcquisitionDetail(userId, acquisitionId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getAcquisitionDetailResponse));
	}

	@DeleteMapping("/{acquisitionId}")
	public ResponseEntity<SuccessResponse<Void>> deleteAcquisition(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long acquisitionId
	){
		acquisitionService.deleteAcquisition(userId, acquisitionId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
	}

	@GetMapping
	public ResponseEntity<SuccessResponse<GetAcquisitionListResponse>> getAllAcquisitions(
		@AuthenticationPrincipal Long userId
	){
		List<GetAcquisitionResponse> getAcquisitionResponses = acquisitionService.getAcquisitionList(userId);
		GetAcquisitionListResponse getAcquisitionListResponse = GetAcquisitionListResponse.of(getAcquisitionResponses);

		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getAcquisitionListResponse));
	}

}
