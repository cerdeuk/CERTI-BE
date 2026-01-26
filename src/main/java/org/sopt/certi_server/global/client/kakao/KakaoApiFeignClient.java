package org.sopt.certi_server.global.client.kakao;

import org.sopt.certi_server.domain.user.dto.response.kakao.KakaoUserInformationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoFeignClient", url = "https://kapi.kakao.com")
public interface KakaoApiFeignClient {

    @PostMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserInformationResponse getInformation(@RequestHeader("Authorization") String accessToken);
}
