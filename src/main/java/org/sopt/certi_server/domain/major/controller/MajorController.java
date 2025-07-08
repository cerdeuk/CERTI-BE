package org.sopt.certi_server.domain.major.controller;

import org.sopt.certi_server.domain.major.dto.response.GetMajorListResponse;
import org.sopt.certi_server.domain.major.service.MajorService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/major")
public class MajorController {
	private final MajorService majorService;

	@GetMapping("/search")
	public ResponseEntity<SuccessResponse<GetMajorListResponse>> searchMajor(
		@RequestParam(name = "keyword") String keyword
	){
		GetMajorListResponse getMajorListResponse = majorService.getMajorList(keyword);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getMajorListResponse));
	}
}
