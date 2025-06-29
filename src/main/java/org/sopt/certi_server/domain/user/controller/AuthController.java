package org.sopt.certi_server.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.request.LoginUriRequest;
import org.sopt.certi_server.domain.user.dto.response.LoginSuccessResponse;
import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.domain.user.service.AuthService;
import org.sopt.certi_server.domain.user.service.SocialService;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping(value = "login-uri")
    public ResponseEntity<SuccessResponse<LoginUriResponse>> processLoginUri(@Valid LoginUriRequest request){
        SocialType socialType = SocialType.from(request.socialType());
        SocialService socialService = authService.getSocialServiceByType(socialType);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, socialService.getAuthorizationUri()));
    }

}
