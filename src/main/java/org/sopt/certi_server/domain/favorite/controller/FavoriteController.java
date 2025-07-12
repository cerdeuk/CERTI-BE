package org.sopt.certi_server.domain.favorite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.favorite.dto.response.FavoriteCertificationSimpleListResponse;
import org.sopt.certi_server.domain.favorite.service.FavoriteService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/home/favorite")
@Tag(name = "Favorite 컨트롤러", description = "즐겨찾기와 관련된 API를 처리합니다.")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    @Operation(summary = "즐겨찾기한 자격증 리스트 조회 API", description = "즐겨찾기한 자격증 리스트를 조회합니다")
    public ResponseEntity<SuccessResponse<FavoriteCertificationSimpleListResponse>> getFavoriteCertifications(
            @AuthenticationPrincipal Long userId
    ){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, favoriteService.getFavoriteCertificationsByUserId(userId)));
    }

}
