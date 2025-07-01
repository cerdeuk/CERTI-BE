package org.sopt.certi_server.domain.user.dto.response;

import org.sopt.certi_server.domain.user.dto.kakao.response.KakaoUserInformationResponse;

public record UserInformation(
        String email,
        String nickname,
        String profileImageUrl
) {
    public static UserInformation from(
            KakaoUserInformationResponse information
    ){
        return new UserInformation(
                information.kakaoAccount().email(),
                information.kakaoAccount().profile().nickname(),
                information.kakaoAccount().profile().profileImageUrl()
        );
    }
}
