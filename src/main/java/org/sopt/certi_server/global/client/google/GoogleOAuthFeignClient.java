package org.sopt.certi_server.global.client.google;

import org.sopt.certi_server.domain.user.dto.response.google.GoogleOAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleOAuthFeignClient", value = "https://oauth2.googleapis.com")
public interface GoogleOAuthFeignClient {

    @PostMapping(value = "/token")
    GoogleOAuthResponse getToken(
            @RequestParam("code") String code,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("grant_type") String grantType
    );
}
