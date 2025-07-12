package org.sopt.certi_server.domain.activity.controller;

import java.util.List;

import org.sopt.certi_server.domain.activity.dto.request.CreateActivityRequest;
import org.sopt.certi_server.domain.activity.dto.response.ActivityDetailResponse;
import org.sopt.certi_server.domain.activity.dto.response.GetActivityListResponse;
import org.sopt.certi_server.domain.activity.service.ActivityService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activity")
@Tag(name = "Activity 컨트롤러", description = "대내외 활동과 관련된 API를 처리합니다.")
public class ActivityController {
	private final ActivityService activityService;

	@PostMapping
	@Operation(summary = "대내외활동 추가 API", description = "대내외 활동을 추가합니다")
	public ResponseEntity<SuccessResponse> createActivity(
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody CreateActivityRequest request) {
		activityService.createActivity(userId, request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@GetMapping
	@Operation(summary = "대내외활동 리스트 조회 API", description = "대내외 활동 리스트를 조회합니다")
	public ResponseEntity<SuccessResponse<GetActivityListResponse>> getAllActivities(
		@AuthenticationPrincipal Long userId
	){
		List<ActivityDetailResponse> activityDetailResponseList = activityService.getActivityList(userId);
		GetActivityListResponse activityListResponse = GetActivityListResponse.of(activityDetailResponseList);

		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, activityListResponse));
	}

	@DeleteMapping("/{activity-id}")
	@Operation(summary = "대내외 활동 삭제 API", description = "대내외 활동을 삭제합니다")
	public ResponseEntity<SuccessResponse> deleteActivity(
		@AuthenticationPrincipal Long userId,
		@Parameter(description = "activity Id", example = "1")
		@PathVariable(name = "activity-id") Long activityId
	){
		activityService.deleteActivity(activityId, userId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
	}

}
