package org.sopt.certi_server.domain.user.service;

import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoService implements SocialService{

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Override
    public LoginUriResponse getAuthorizationUri() {
        String uri = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" +
                kakaoClientId +
                "&redirect_uri=" +
                kakaoRedirectUri;

        return LoginUriResponse.of(uri);
    }
}
