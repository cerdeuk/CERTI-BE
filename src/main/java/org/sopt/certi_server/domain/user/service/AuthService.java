package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.response.AuthResponse;
import org.sopt.certi_server.domain.user.dto.response.JwtResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.jwt.domain.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final KakaoService kakaoService;

    public AuthResponse login(OAuthUserInformation userInfo){
        return userRepository.findByEmail(userInfo.email())
                .map(this::handleExistingUser)
                .orElseGet(() -> handleNewUser(userInfo));
    }

    private AuthResponse handleNewUser(OAuthUserInformation userInfo) {
        String preSignupToken = jwtService.generatePreSignupToken(userInfo.email());
        return AuthResponse.ofNotRegisteredUser(preSignupToken, userInfo);
    }

    private AuthResponse handleExistingUser(User user) {
        JwtResponse jwtResponse = jwtService.issueToken(user.getId());
        return AuthResponse.ofRegisteredUser(jwtResponse);
    }

    @Transactional
    public AuthResponse register(String authorization, OAuthUserInformation userInfo) {

        jwtService.validatePreSignupToken(authorization);

        User newUser = User.createUser(userInfo.nickname(), userInfo.email(), userInfo.profileImageUrl());
        userRepository.save(newUser);

        JwtResponse token = jwtService.issueToken(newUser.getId());
        return AuthResponse.ofRegisteredUser(token);
    }

    public JwtResponse reIssueToken(String authorization) {
        return jwtService.reissueToken(authorization);
    }

    public SocialService getSocialServiceByType(SocialType socialType) {
        return switch (socialType){
            case KAKAO -> kakaoService;
            case APPLE -> null;
        };
    }


}
