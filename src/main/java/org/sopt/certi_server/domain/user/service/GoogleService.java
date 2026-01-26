package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.global.client.google.GoogleApiFeignClient;
import org.sopt.certi_server.global.client.google.GoogleOAuthFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoogleService implements SocialService{

    private final GoogleOAuthFeignClient googleOAuthFeignClient;
    private final GoogleApiFeignClient googleApiFeignClient;

    @Value("${google.id}")
    private String googleClientId;

    @Value("${google.secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Override
    public LoginUriResponse getAuthorizationUri() {
        return null;
    }

    @Override
    public OAuthUserInformation getUserInfo(String code) {
        return null;
    }

    @Override
    public OAuthUserInformation getUserInfoByAccessToken(String token) {
        return null;
    }
}
