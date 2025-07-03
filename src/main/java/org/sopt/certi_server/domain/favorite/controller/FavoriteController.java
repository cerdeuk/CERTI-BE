package org.sopt.certi_server.domain.favorite.controller;

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
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<SuccessResponse<FavoriteCertificationSimpleListResponse>> getFavoriteCertifications(
            @AuthenticationPrincipal Long userId
    ){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, favoriteService.getFavoriteCertificationsByUserId(userId)));
    }

}
