package org.sopt.certi_server.domain.user.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.user.dto.request.google.GoogleTokenRequest;
import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.domain.user.dto.response.google.GoogleOAuthResponse;
import org.sopt.certi_server.domain.user.dto.response.google.GoogleUserInformation;
import org.sopt.certi_server.global.client.google.GoogleApiFeignClient;
import org.sopt.certi_server.global.client.google.GoogleOAuthFeignClient;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

            // 구글은 access token을 클라이언트에서 발급받는 방식이 anti pattern
            // 따라서 id token을 분석해 사용자 정보를 추출한다.
            return getUserInfoByIdToken(token);
        }catch (Exception e) {
            log.error("google user data 획득 실패: {}", e.getMessage());
            throw e;
        }
    }

    // access token이 아닌, id token 방식
    public OAuthUserInformation getUserInfoByIdToken(String idToken){

        GoogleUserInformation googleUserInformation = verifyToken(idToken);

        return OAuthUserInformation.from(googleUserInformation);
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

    public GoogleUserInformation verifyToken(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // 내 앱의 Client ID를 설정 (중요: 다른 앱에서 발급된 토큰을 차단함)
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            // 1. 토큰 검증 (서명 확인, 만료 여부 확인 등)
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                // 2. 복호화된 페이로드(내용물) 가져오기
                GoogleIdToken.Payload payload = idToken.getPayload();

                // 3. 사용자 정보 추출
                String userId = payload.getSubject();    // 구글의 유니크한 사용자 ID
                String email = payload.getEmail();       // 이메일
                String name = (String) payload.get("name"); // 이름
                String pictureUrl = (String) payload.get("picture"); // 프로필 사진

                log.info("User ID: {}", userId);
                log.info("Email: {}", email);

                return new GoogleUserInformation(userId, name, null, null, pictureUrl, email, false, null);
            } else {
                System.out.println("유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            log.error("구글 ID 토큰 파싱 실패");
            e.printStackTrace();
        }
        throw new BusinessException(ErrorCode.BAD_REQUEST_DATA);
    }
}
