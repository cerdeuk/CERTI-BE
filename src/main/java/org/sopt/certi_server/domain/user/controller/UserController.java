package org.sopt.certi_server.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.user.dto.request.UpdateJobRequest;
import org.sopt.certi_server.domain.user.dto.request.UpdateUserRequest;
import org.sopt.certi_server.domain.user.dto.response.GetJobResponse;
import org.sopt.certi_server.domain.user.dto.response.GetMyPageInfoResponse;
import org.sopt.certi_server.domain.user.dto.response.GetUserResponse;
import org.sopt.certi_server.domain.user.dto.response.PersonalInformationResponse;
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
    public ResponseEntity<SuccessResponse<GetMyPageInfoResponse>> getMyPageHomeInfo(
            @AuthenticationPrincipal Long userId
    ){

        GetMyPageInfoResponse getMyPageInfoResponse = userService.getMyPageInfoResponse(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getMyPageInfoResponse));
    }

    @GetMapping(value = "/pinfo")
    @Operation(summary = "개인정보 수정 페이지 조회 API", description = "개인정보 수정 페이지를 조회합니다.")
    public ResponseEntity<SuccessResponse<PersonalInformationResponse>> getPersonalInformation(
            @AuthenticationPrincipal Long userId
    ){
        PersonalInformationResponse pInformationResponse = userService.getPersonalInformationResponse(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, pInformationResponse));
    }

    @PutMapping(value = "/pinfo")
    @Operation(summary = "개인정보 수정 API", description = "개인정보를 수정합니다.")
    public ResponseEntity<SuccessResponse<Void>> putPersonalInformation(
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdateUserRequest request
    ){
        userService.updateUserInformation(userId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
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

    @GetMapping(value = "/validation")
    @Operation(summary = "닉네임 검증 API", description = "닉네임이 중복이거나 욕설이 포함되어 있는지 검사합니다.")
    public ResponseEntity<SuccessResponse<Void>> validateNickname(
            @RequestParam(value = "keyword") String nickname
    ){
        userService.validateNickname(nickname);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH));
    }

    @PutMapping(value = "/university")
    @Operation(summary = "대학교 변경 API", description = "대학교 정보를 변경합니다.")
    public ResponseEntity<SuccessResponse<Void>> updateUniversity(
            @AuthenticationPrincipal Long userId,
            @RequestBody String universityName
    ){
        userService.changeUniversity(userId, universityName);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }

    @PutMapping(value = "/major")
    @Operation(summary = "학과 변경 API", description = "학과 정보를 변경합니다.")
    public ResponseEntity<SuccessResponse<Void>> updateMajor(
            @AuthenticationPrincipal Long userId,
            @RequestBody String majorName
    ){
        userService.changeMajor(userId, majorName);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }
}
