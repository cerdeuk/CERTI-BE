package org.sopt.certi_server.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.user.dto.request.UpdateJobRequest;
import org.sopt.certi_server.domain.user.dto.response.GetJobResponse;
import org.sopt.certi_server.domain.user.dto.response.GetMyPageInfoResponse;
import org.sopt.certi_server.domain.user.dto.response.GetUserResponse;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
@Tag(name = "User 컨트롤러", description = "사용자와 관련된 API를 처리합니다.")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "사용자 정보 조회 API", description = "홈화면에서의 사용자 정보를 조회합니다.")
    public ResponseEntity<SuccessResponse<GetUserResponse>> getHomeUser(
            @AuthenticationPrincipal Long userId
    ) {
        log.info("getHomeUser: userId={}", userId);
        GetUserResponse getUserResponse = userService.getHomeUser(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getUserResponse));
    }

    @GetMapping(value = "/mypage")
    @Operation(summary = "마이 페이지 홈 API", description = "마이페이지 홈 화면을 조회합니다.")
    public ResponseEntity<SuccessResponse> getMyPageHomeInfo(
            @AuthenticationPrincipal Long userId
    ){

        GetMyPageInfoResponse getMyPageInfoResponse = userService.getMyPageInfoResponse(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getMyPageInfoResponse));
    }

    @GetMapping("/job")
    @Operation(summary = "희망직무 조회 API", description = "사용자의 희망직무를 조회합니다")
    public ResponseEntity<SuccessResponse<GetJobResponse>> getUserJob(
            @AuthenticationPrincipal Long userId
    ) {
        GetJobResponse jobResponse = userService.getUserJob(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, jobResponse));
    }

    @PostMapping("/job")
    @Operation(summary = "희망직무 수정 API", description = "사용자의 희망직무를 수정합니다.")
    public ResponseEntity<SuccessResponse> updateUserJob(
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdateJobRequest updateJobRequest
    ) {
        userService.updateUserJob(userId, updateJobRequest.jobNameList());
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }
}
