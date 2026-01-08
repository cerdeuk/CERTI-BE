package org.sopt.certi_server.domain.acquisition.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.acquisition.dto.request.CreateAcquisitionRequest;
import org.sopt.certi_server.domain.acquisition.dto.request.PatchAcquisitionRequest;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionDetailResponse;
import org.sopt.certi_server.domain.acquisition.dto.response.GetAcquisitionResponse;
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
@Tag(name = "Acquisition 컨트롤러", description = "취득한 자격증과 관련된 API를 처리합니다.")
public class AcquisitionController {
    private final AcquisitionService acquisitionService;

    @PostMapping
    @Operation(summary = "취득한 자격증 추가 API", description = "취득한 자격증을 추가합니다")
    public ResponseEntity<SuccessResponse<Boolean>> addAcquisition(
            @AuthenticationPrincipal Long userId,
            @RequestBody CreateAcquisitionRequest request
            ) {
        boolean isAcquired = acquisitionService.createAcquisition(userId, request);

        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE, isAcquired));
    }

    @GetMapping(value = "/{acquisitionId}")
    @Operation(summary = "취득한 자격증 상세 조회 API", description = "취득한 자격증을 상세 조회합니다")
    public ResponseEntity<SuccessResponse<GetAcquisitionDetailResponse>> getAcquisition(
            @Parameter(description = "acquisition Id", example = "1")
            @PathVariable(name = "acquisitionId") Long acquisitionId
    ) {
        GetAcquisitionDetailResponse getAcquisitionDetailResponse = acquisitionService.getAcquisitionDetail(acquisitionId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getAcquisitionDetailResponse));
    }

    @PatchMapping(value = "/{acquisitionId}")
    @Operation(summary = "취득한 자격증 정보 수정 API", description = "취득한 자격증 정보를 수정합니다.")
    public ResponseEntity<SuccessResponse<Void>> modifyAcquisition(
            @AuthenticationPrincipal Long userId,
            @RequestBody PatchAcquisitionRequest request,
            @PathVariable(name = "acquisitionId") Long acquisitionId
    ){
        acquisitionService.patchAcquisition(userId, acquisitionId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
    }

    @DeleteMapping(value = "/{acquisitionId}")
    @Operation(summary = "취득한 자격증 삭제 API", description = "취득한 자격증을 상세 조회합니다")
    public ResponseEntity<SuccessResponse<Void>> deleteAcquisition(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "acquisition Id", example = "1")
            @PathVariable Long acquisitionId
    ) {
        acquisitionService.deleteAcquisition(userId, acquisitionId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
    }

    @GetMapping
    @Operation(summary = "취득한 자격증 리스트 조회 API", description = "취득한 자격증 리스트를 조회합니다")
    public ResponseEntity<SuccessResponse<GetAcquisitionListResponse>> getAllAcquisitions(
            @AuthenticationPrincipal Long userId
    ) {
        List<GetAcquisitionResponse> getAcquisitionResponses = acquisitionService.getAcquisitionList(userId);
        GetAcquisitionListResponse getAcquisitionListResponse = GetAcquisitionListResponse.of(getAcquisitionResponses);

        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getAcquisitionListResponse));
    }

}
