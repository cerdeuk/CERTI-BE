package org.sopt.certi_server.domain.major.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.major.dto.response.GetMajorListResponse;
import org.sopt.certi_server.domain.major.service.MajorService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/major")
@Tag(name = "major 컨트롤러", description = "전공과 관련된 API를 처리합니다.")
public class MajorController {
    private final MajorService majorService;

    @GetMapping("/search")
    @Operation(summary = "학과 검색 API", description = "학과를 검색합니다")
    public ResponseEntity<SuccessResponse<GetMajorListResponse>> searchMajor(
            @Parameter(description = "keyword", example = "컴퓨터공학과")
            @RequestParam(name = "keyword") String keyword
    ) {
        GetMajorListResponse getMajorListResponse = majorService.getMajorList(keyword);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, getMajorListResponse));
    }
}
