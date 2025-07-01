package org.sopt.certi_server.domain.user.dto.kakao.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserInformationResponse(
        Long id,
        KakaoAccount kakaoAccount) {

    public record KakaoAccount(
            @JsonProperty("profile") KakaoProfile profile,
            String email
    ){

    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record KakaoProfile(
            String nickname,
            String profileImageUrl
    ){

    }
}
