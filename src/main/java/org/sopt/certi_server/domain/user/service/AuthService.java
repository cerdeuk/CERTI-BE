package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.response.AuthResponse;
import org.sopt.certi_server.domain.user.dto.response.JwtResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.error.exception.UnauthorizedException;
import org.sopt.certi_server.global.jwt.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;

    public AuthResponse login(OAuthUserInformation userInfo){
        return userRepository.findByEmail(userInfo.email())
                .map(this::handleExistingUser)
                .orElseGet(() -> handleNewUser(userInfo));
    }

    private AuthResponse handleNewUser(OAuthUserInformation userInfo) {
        String preSignupToken = jwtUtil.createPreSignupToken(userInfo.email());
        return AuthResponse.ofNotRegisteredUser(preSignupToken, userInfo);
    }

    private AuthResponse handleExistingUser(User user) {
        JwtResponse jwtResponse = issueToken(user.getId());
        return AuthResponse.ofRegisteredUser(jwtResponse);
    }

    @Transactional
    public AuthResponse register(String authorization, OAuthUserInformation userInfo) {

        String preSignupToken = jwtUtil.extractToken(authorization);

        if(jwtUtil.isTokenExpired(preSignupToken)){
            throw new UnauthorizedException();
        }

        User newUser = User.createUser(userInfo.nickname(), userInfo.email(), userInfo.profileImageUrl());
        userRepository.save(newUser);
        JwtResponse token = issueToken(newUser.getId());
        return AuthResponse.ofRegisteredUser(token);
    }

    public JwtResponse reIssueToken(String authorization) {
        String refreshToken = jwtUtil.extractToken(authorization);

        boolean tokenExpired = jwtUtil.isTokenExpired(refreshToken);
        if(tokenExpired){
            throw new UnauthorizedException();
        }
        Long userId = jwtUtil.getUserId(refreshToken);
        return issueToken(userId);
    }

    public SocialService getSocialServiceByType(SocialType socialType) {
        return switch (socialType){
            case KAKAO -> kakaoService;
            case APPLE -> null;
        };
    }

    public JwtResponse issueToken(Long userId){
        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);
        return JwtResponse.of(accessToken, refreshToken);
    }
}
