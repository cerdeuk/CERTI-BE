package org.sopt.certi_server.global.client.google;

import org.sopt.certi_server.domain.user.dto.response.google.GoogleUserInformation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleUserInfoFeignClient", url = "https://www.googleapis.com")
public interface GoogleApiFeignClient {

    @GetMapping(value = "/oauth2/v3/userinfo")
    GoogleUserInformation getUserInfo(
            @RequestHeader("Authorization") String accessToken
    );
}
