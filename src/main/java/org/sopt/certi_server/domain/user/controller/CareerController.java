package org.sopt.certi_server.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.request.CreateCareerRequest;
import org.sopt.certi_server.domain.user.dto.request.UpdateCareerRequest;
import org.sopt.certi_server.domain.user.dto.response.CareerDetailResponse;
import org.sopt.certi_server.domain.user.dto.response.GetCareersReponse;
import org.sopt.certi_server.domain.user.service.CareerService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/careers")
@Tag(name = "Career 컨트롤러", description = "경력사항과 관련된 API를 처리합니다.")
public class CareerController {
    private final CareerService careerService;

    @GetMapping
    @Operation(summary = "경력사항 리스트 조회 API", description = "사용자의 경력사항 리스트를 조회합니다")
    public ResponseEntity<SuccessResponse<GetCareersReponse>> getCareers(
            @AuthenticationPrincipal Long userId
    ) {
        List<CareerDetailResponse> careerDetailResponseList = careerService.getCareerList(userId);
        GetCareersReponse getCareersReponse = GetCareersReponse.of(careerDetailResponseList);

        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getCareersReponse));
    }

    @PostMapping
    @Operation(summary = "경력사항 추가 API", description = "사용자의 경력사항을 추가합니다")
    public ResponseEntity<SuccessResponse> createCareer(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateCareerRequest request
    ) {
        careerService.createCareer(userId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @PutMapping("/{careerId}")
    @Operation(summary = "경력사항 수정 API", description = "사용자의 경력사항을 수정합니다")
    public ResponseEntity<SuccessResponse> updateCareer(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long careerId,
        @Valid @RequestBody UpdateCareerRequest request
    ) {
        careerService.updateCareer(userId, careerId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
    }

    @DeleteMapping("/{career-id}")
    @Operation(summary = "경력사항 삭제 API", description = "사용자의 경력사항을 삭제합니다")
    public ResponseEntity<SuccessResponse> deleteCareer(
            @AuthenticationPrincipal Long userId,
            @PathVariable("career-id") Long careerId
    ) {
        careerService.deleteCareer(userId, careerId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
    }
}
