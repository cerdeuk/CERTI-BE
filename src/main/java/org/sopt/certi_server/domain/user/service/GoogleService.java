package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.user.dto.request.google.GoogleTokenRequest;
import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.domain.user.dto.response.google.GoogleOAuthResponse;
import org.sopt.certi_server.domain.user.dto.response.google.GoogleUserInformation;
import org.sopt.certi_server.global.client.google.GoogleApiFeignClient;
import org.sopt.certi_server.global.client.google.GoogleOAuthFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class GoogleService implements SocialService{

    private final GoogleOAuthFeignClient googleOAuthFeignClient;
    private final GoogleApiFeignClient googleApiFeignClient;

    @Value("${google.id}")
    private String googleClientId;

    @Value("${google.secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    private static final String GOOGLE_AUTH_URI = "https://accounts.google.com/o/oauth2/v2/auth";

    @Override
    public LoginUriResponse getAuthorizationUri() {
        String uri = GOOGLE_AUTH_URI +
                "?client_id=" + googleClientId +
                "&redirect_uri=" + googleRedirectUri +
                "&response_type=code" +
                "&scope=openid%20email%20profile";

        return LoginUriResponse.of(uri);
    }

    @Override
    public OAuthUserInformation getUserInfo(String code) {

        GoogleOAuthResponse oauth = getOAuthToken(code);
        log.info("oauth info: {}", oauth);
        String accessToken = oauth.accessToken();
        log.info("google oauth access token: {}", accessToken);
        return getUserInfoByAccessToken(accessToken);
    }

    @Override
    public OAuthUserInformation getUserInfoByAccessToken(String token) {
        try{
            log.info("google access token: {}", token);
            GoogleUserInformation information = googleApiFeignClient.getUserInfo("Bearer " + token);
            return OAuthUserInformation.from(information);
        }catch (Exception e) {
            log.error("google user data 획득 실패: {}", e.getMessage());
            throw e;
        }
    }

    public GoogleOAuthResponse getOAuthToken(String code){
        try{
            return googleOAuthFeignClient.getToken(
                code,
                googleClientId,
                googleClientSecret,
                googleRedirectUri,
                "authorization_code",
                    ""
            );
        }catch (Exception e) {
            log.error("google oauth token 발급 실패: {}", e.getMessage());
            throw e;
        }
    }
}
