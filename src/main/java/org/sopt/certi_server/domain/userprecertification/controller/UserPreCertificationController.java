package org.sopt.certi_server.domain.userprecertification.controller;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.userprecertification.dto.request.CreateUserPreCertificationRequest;
import org.sopt.certi_server.domain.userprecertification.dto.request.PatchPreCertificationRequest;
import org.sopt.certi_server.domain.userprecertification.dto.response.DayScheduleRes;
import org.sopt.certi_server.domain.userprecertification.dto.response.MonthCalendarRes;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimple;
import org.sopt.certi_server.domain.userprecertification.dto.response.PreCertificationSimpleListResponse;
import org.sopt.certi_server.domain.userprecertification.service.UserPreCertificationService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/home/pre-certification")
@RequiredArgsConstructor
@Tag(name = "UserPreCertificationController 컨트롤러", description = "취득예정과 관련된 API를 처리합니다.")
public class UserPreCertificationController {

    private final UserPreCertificationService userPreCertificationService;

    @GetMapping
    @Operation(summary = "취득예정 자격증 리스트 조회 API", description = "취득예정 자격증 리스트를 조회합니다")
    public ResponseEntity<SuccessResponse<PreCertificationSimpleListResponse>> getPreCertificationListData(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, userPreCertificationService.getPreCertificationListDataByUserId(userId)));
    }

    @PostMapping
    @Operation(summary = "취득예정 자격증 추가 API", description = "취득예정 자격증을 추가합니다")
    public ResponseEntity<SuccessResponse<?>> addPreCertification(
            @AuthenticationPrincipal Long userId,
            @RequestBody CreateUserPreCertificationRequest request
    ) {
        boolean isPreCertificated = userPreCertificationService.createNewPreCertification(userId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE, isPreCertificated));
    }

    @PatchMapping(value = "/{userPreCertificationId}")
    @Operation(summary = "취득예정 정보를 수정 API", description = "취득예정 정보를 수정합니다.")
    public ResponseEntity<SuccessResponse<Void>> modifyPreCertification(
            @AuthenticationPrincipal Long userId,
            @RequestBody PatchPreCertificationRequest request,
            @PathVariable(name = "userPreCertificationId") Long userPreCertificationId
    ){
        userPreCertificationService.patchPreCertification(userId, userPreCertificationId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }

    @DeleteMapping(value = "/{certificationId}")
    @Operation(summary = "취득예정 자격증 삭제 API", description = "취득예정 자격증을 삭제합니다")
    public ResponseEntity<SuccessResponse<Void>> deletePreCertification(
            @AuthenticationPrincipal @NotNull(message = "인증되지 않은 사용자입니다.") Long userId,
            @Parameter(description = "certification Id", example = "1")
            @PathVariable Long certificationId
    ) {
        userPreCertificationService.deletePreCertification(userId, certificationId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
    }

    @GetMapping("/month")
    @Operation(summary = "월별 취득예정 자격증 조회", description = "캘린더에서 월별 취득예정 자격증을 조회합니다")
    public ResponseEntity<SuccessResponse<MonthCalendarRes>> getMonth(
        @AuthenticationPrincipal Long userId,
        @RequestParam(required = true) int year,
        @RequestParam(required = true) int month
    ){
        MonthCalendarRes monthCalendarRes = userPreCertificationService.getMonthCalendar(userId, year, month);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, monthCalendarRes));
    }

    @GetMapping("/day")
    @Operation(summary = "일별 취득예정 자격증 조회", description = "캘린더에서 일별 취득예정 자격증을 조회합니다")
    public ResponseEntity<SuccessResponse<DayScheduleRes>> getDay(
        @AuthenticationPrincipal Long userId,
        @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        DayScheduleRes dayScheduleRes = userPreCertificationService.getDaySchedules(userId, date);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, dayScheduleRes));
    }
}
