package org.sopt.certi_server.domain.user.dto.response;

import org.sopt.certi_server.domain.user.dto.response.kakao.KakaoUserInformationResponse;

public record OAuthUserInformation(
        String email,
        String nickname,
        String profileImageUrl
) {
    public static OAuthUserInformation from(
            KakaoUserInformationResponse information
    ){
        return new OAuthUserInformation(
                information.kakaoAccount().email(),
                information.kakaoAccount().profile().nickname(),
                information.kakaoAccount().profile().profileImageUrl()
        );
    }
}
