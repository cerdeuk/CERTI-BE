package org.sopt.certi_server.domain.user.dto.response;

import jakarta.validation.constraints.NotEmpty;
import org.sopt.certi_server.domain.user.dto.response.kakao.KakaoUserInformationResponse;

public record OAuthUserInformation(
        String email,
        @NotEmpty(message = "사용자 닉네임 정보는 필수입니다.") String nickname,
        String profileImageUrl
) {
    public static OAuthUserInformation from(
            KakaoUserInformationResponse information
    ) {
        return new OAuthUserInformation(
                information.kakaoAccount().email(),
                information.kakaoAccount().profile().nickname(),
                information.kakaoAccount().profile().profileImageUrl()
        );
    }
}
