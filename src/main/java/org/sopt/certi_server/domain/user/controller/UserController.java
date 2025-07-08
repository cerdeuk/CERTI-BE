package org.sopt.certi_server.domain.user.controller;

import org.sopt.certi_server.domain.user.dto.request.UpdateJobRequest;
import org.sopt.certi_server.domain.user.dto.response.GetJobResponse;
import org.sopt.certi_server.domain.user.dto.response.GetUserResponse;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
	private final UserService userService;

	@GetMapping
	public ResponseEntity<SuccessResponse<GetUserResponse>> getHomeUser(
		@AuthenticationPrincipal Long userId
		){
		GetUserResponse getUserResponse = userService.getHomeUser(userId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getUserResponse));
	}

	@GetMapping("/job")
	public ResponseEntity<SuccessResponse<GetJobResponse>> getUserJob(
		//@AuthenticationPrincipal Long userId
	){
		GetJobResponse jobResponse = userService.getUserJob(1L);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, jobResponse));
	}
	@PostMapping("/job")
	public ResponseEntity<SuccessResponse> updatehUserJob(
		//@AuthenticationPrincipal Long userId,
		@RequestBody UpdateJobRequest updateJobRequest
	){
		userService.updateUserJob(1L, updateJobRequest.jobNameList());
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
	}
}
