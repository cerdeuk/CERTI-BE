package org.sopt.certi_server.domain.career.controller;

import java.util.List;

import org.sopt.certi_server.domain.career.dto.request.CreateCareerRequest;
import org.sopt.certi_server.domain.career.dto.response.CareerDetailResponse;
import org.sopt.certi_server.domain.career.dto.response.GetCareersReponse;
import org.sopt.certi_server.domain.career.service.CareerService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/careers")
public class CareerController {
	private final CareerService careerService;

	@GetMapping
	public ResponseEntity<SuccessResponse<GetCareersReponse>> getCareers(
		@AuthenticationPrincipal Long userId
		){
		List<CareerDetailResponse> careerDetailResponseList = careerService.getCareerList(userId);
		GetCareersReponse getCareersReponse = GetCareersReponse.of(careerDetailResponseList);

		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getCareersReponse));
	}

	@PostMapping
	public ResponseEntity<SuccessResponse> createCareer(
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody CreateCareerRequest request
	){
		careerService.createCareer(userId, request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@DeleteMapping("/{career-id}")
	public ResponseEntity<SuccessResponse> deleteCareer(
		@AuthenticationPrincipal Long userId,
		@PathVariable("career-id") Long careerId
	){
		careerService.deleteCareer(userId, careerId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
	}
}
