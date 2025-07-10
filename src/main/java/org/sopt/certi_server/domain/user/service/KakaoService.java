package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.user.dto.response.kakao.KakaoOAuthResponse;
import org.sopt.certi_server.domain.user.dto.response.kakao.KakaoUserInformationResponse;
import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.global.client.KakaoApiFeignClient;
import org.sopt.certi_server.global.client.KakaoOAuthFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService implements SocialService{

    private final KakaoApiFeignClient kakaoApiFeignClient;
    private final KakaoOAuthFeignClient kakaoOAuthFeignClient;

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

    @Override
    public OAuthUserInformation getUserInfo(String code) {

        KakaoOAuthResponse oauth = getOAuthToken(code);
        log.info("oauth info: {}", oauth);
        String accessToken = oauth.accessToken();
        log.info("kakao oauth access token: {}", accessToken);

        return getUserInfoByAccessToken(accessToken);

    }

    @Override
    public OAuthUserInformation getUserInfoByAccessToken(String accessToken) {
        try{
            KakaoUserInformationResponse information = kakaoApiFeignClient.getInformation("Bearer " + accessToken);
            log.info(information.kakaoAccount().profile().nickname());
            log.info(information.kakaoAccount().email());
            return OAuthUserInformation.from(information);
        }catch (Exception e){
            log.error("kakao user data 획득 실패: {}", e.getMessage());
            throw e;
        }
    }

    public KakaoOAuthResponse getOAuthToken(String code){
        try{
            return kakaoOAuthFeignClient.getToken(
                    "authorization_code",
                    kakaoClientId,
                    kakaoRedirectUri,
                    code
                    );
        }catch (Exception e){
            log.error("kakao oauth token 발급 실패: {}", e.getMessage());
            throw e;
        }
    }

}
