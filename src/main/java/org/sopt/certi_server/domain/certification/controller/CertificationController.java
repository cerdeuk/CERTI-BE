package org.sopt.certi_server.domain.certification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.dto.response.CertificationDetailResponse;
import org.sopt.certi_server.domain.certification.dto.response.CertificationListResponse;
import org.sopt.certi_server.domain.certification.dto.response.CertificationRankResponse;
import org.sopt.certi_server.domain.certification.dto.response.CertificationRecommendationListResponse;
import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.favorite.service.FavoriteService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/certification")
@Tag(name = "Certification 컨트롤러", description = "자격증과 관련된 API를 처리합니다.")
public class CertificationController {

    private final CertificationService certificationService;
    private final FavoriteService favoriteService;

    @GetMapping(value = "/{certificationId}")
    @Operation(summary = "자격증 조회 API", description = "자격증을 조회합니다")
    public ResponseEntity<SuccessResponse<CertificationDetailResponse>> getCertification(
            @Parameter(description = "certificatio Id", example = "1")
            @PathVariable Long certificationId) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, certificationService.getCertificationDetail(certificationId)));
    }

    @GetMapping(value = "/search")
    @Operation(summary = "자격증 검색 API", description = "자격증을 검색합니다")
    public ResponseEntity<SuccessResponse<CertificationListResponse>> searchCertification(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "keyword", example = "정보처리기사")
            @RequestParam(value = "keyword") String keyword
    ) {
        List<CertificationSimple> certificationSimpleList = certificationService.searchCertification(userId, keyword);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, CertificationListResponse.of(certificationSimpleList)));
    }

    @PostMapping(value = "/{certificationId}/favorite")
    @Operation(summary = "자격증 즐겨찾기 API", description = "자격증을 즐겨찾기합니다")
    public ResponseEntity<SuccessResponse<Void>> toggleFavorite(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "certification Id", example = "1")
            @PathVariable Long certificationId
    ) {
        boolean isCreated = favoriteService.toggleFavorite(userId, certificationId);
        if (isCreated) {
            return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
        } else {
            return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
        }
    }

    @GetMapping("/jobs")
    @Operation(summary = "직무별 자격증 조회 API", description = "직무별로 자격증 리스트를 조회합니다")
    public ResponseEntity<SuccessResponse<?>> getCertificationByJobList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "isFavorite") Boolean isFavorite,
            @RequestParam(value = "jobs") String job
    ) {
        CertificationListResponse certificationListResponse = certificationService.getCertificationByJobList(userId, isFavorite, job);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, certificationListResponse));
    }

    @GetMapping("/tracks")
    @Operation(summary = "계열별 자격증 조회 API", description = "계열별 자격증 리스트를 조회합니다")
    public ResponseEntity<SuccessResponse<?>> getCertificationByTrackList(
        @AuthenticationPrincipal Long userId,
        @RequestParam(value = "isFavorite") Boolean isFavorite,
        @RequestParam(value = "tracks") String track
    ) {
        CertificationListResponse certificationListResponse = certificationService.getCertificationByTrackList(userId, isFavorite, track);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, certificationListResponse));
    }


    @GetMapping("/recommend")
    @Operation(summary = "자격증 추천 API", description = "추천 자격증을 조회합니다")
    public ResponseEntity<SuccessResponse<CertificationRecommendationListResponse>> recommendCertification(
            @AuthenticationPrincipal Long userId
    ) {
        CertificationRecommendationListResponse certiRecommendListRes = certificationService.recommendCertifications(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, certiRecommendListRes));
    }

    @GetMapping("/job")
    @Operation(summary = "직무별 자격증 조회 API", description = "3순위 직무별 자격증을 조회합니다")
    public ResponseEntity<SuccessResponse<?>> getTop3ByJob(
        @AuthenticationPrincipal Long userId
    ){
        List<CertificationRankResponse> certificationRankResponseList = certificationService.getCertificationJob(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, certificationRankResponseList));
    }

    @GetMapping("/track")
    @Operation(summary = "계열별 자격증 조회 API", description = "3순위 계열별 자격증을 조회합니다")
    public ResponseEntity<SuccessResponse<?>> getTop3ByTrack(
        @AuthenticationPrincipal Long userId
    ) {
        List<CertificationRankResponse> certificationRankResponseList = certificationService.getCertificationTrack(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, certificationRankResponseList));

    }




}
