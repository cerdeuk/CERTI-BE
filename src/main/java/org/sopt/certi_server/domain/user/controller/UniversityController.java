package org.sopt.certi_server.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.response.GetUniversityListResponse;
import org.sopt.certi_server.domain.user.service.UniversityService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/university")
@Tag(name = "University 컨트롤러", description = "대학교와 관련된 API를 처리합니다.")
public class UniversityController {
    private final UniversityService universityService;

    @GetMapping("/search")
    @Operation(summary = "대학교 검색 API", description = "온보딩 과정 중 대학교 검색을 합니다.")
    public ResponseEntity<SuccessResponse<?>> searchUniversity(
            @Parameter(description = "검색할 키워드")
            @RequestParam(name = "keyword") String keyword
    ) {
        GetUniversityListResponse getUniversityListResponse = universityService.getUniversityList(keyword);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getUniversityListResponse));
    }

}
