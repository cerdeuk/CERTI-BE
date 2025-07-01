package org.sopt.certi_server.domain.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.request.LoginRequest;
import org.sopt.certi_server.domain.user.dto.request.LoginUriRequest;
import org.sopt.certi_server.domain.user.dto.request.SignupRequest;
import org.sopt.certi_server.domain.user.dto.response.AuthResponse;
import org.sopt.certi_server.domain.user.dto.response.JwtResponse;
import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.domain.user.service.AuthService;
import org.sopt.certi_server.domain.user.service.SocialService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.code.SuccessCode;
import org.sopt.certi_server.global.error.dto.SuccessResponse;
import org.sopt.certi_server.global.error.exception.ForbiddenException;
import org.sopt.certi_server.global.error.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping(value = "/login-uri")
    public ResponseEntity<SuccessResponse<LoginUriResponse>> processLoginUri(@Valid LoginUriRequest request){
        SocialType socialType = SocialType.from(request.socialType());
        SocialService socialService = authService.getSocialServiceByType(socialType);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, socialService.getAuthorizationUri()));
    }

    @GetMapping(value = "/login")
    public ResponseEntity<SuccessResponse<AuthResponse>> processLogin(@Valid LoginRequest request){
        SocialType socialType = SocialType.from(request.socialType());
        SocialService socialService = authService.getSocialServiceByType(socialType);

        OAuthUserInformation userInfo = socialService.getUserInfo(request.code());

        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, authService.login(userInfo)));

    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<SuccessResponse<AuthResponse>> processSignup(
            @RequestHeader("Authorization") @NotEmpty(message = "해당 api에는 authorization 헤더가 필수입니다.") String authorization,
            @RequestBody SignupRequest request
    ){
        if(authorization == null){
            throw new UnauthorizedException();
        }
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE, authService.register(authorization, request.userInformation())));
    }

    @GetMapping(value = "/reissue")
    public ResponseEntity<SuccessResponse<JwtResponse>> processReissue(
            @RequestHeader("Authorization") @NotEmpty(message = "해당 api에는 authorization 헤더가 필수입니다.") String authorization
    ){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, authService.reIssueToken(authorization)));
    }


}
