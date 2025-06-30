package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.response.UserInformation;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.dto.response.LoginSuccessResponse;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.client.KakaoOAuthFeignClient;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.sopt.certi_server.global.jwt.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;

    @Transactional
    public LoginSuccessResponse login(UserInformation userInfo){
        Optional<User> userOpt = userRepository.findByEmail(userInfo.email());
        // 이미 회원 가입한 회원
        if(userOpt.isPresent()){
            User findUser = userOpt.get();
            String accessToken = jwtUtil.createAccessToken(findUser.getId());
            String refreshToken = jwtUtil.createRefreshToken(findUser.getId());
            return LoginSuccessResponse.of(accessToken, refreshToken);
        }
        // 신규 가입 회원
        else{
            User newUser = User.createUser(userInfo.nickname(), userInfo.email(), userInfo.profileImageUrl());
            userRepository.save(newUser);
            String accessToken = jwtUtil.createAccessToken(newUser.getId());
            String refreshToken = jwtUtil.createRefreshToken(newUser.getId());
            return LoginSuccessResponse.of(accessToken, refreshToken);
        }
    }

    public SocialService getSocialServiceByType(SocialType socialType) {
        return switch (socialType){
            case KAKAO -> kakaoService;
            case APPLE -> null;
        };
    }
}
