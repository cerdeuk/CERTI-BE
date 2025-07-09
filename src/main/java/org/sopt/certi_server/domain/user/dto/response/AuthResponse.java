package org.sopt.certi_server.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
        Long userId,
        String nickName,
        boolean needSignUp,
        String preSignupToken,
        JwtResponse tokenResponse,
        OAuthUserInformation userInformation
) {
    public static AuthResponse ofNotRegisteredUser(String preSignupToken, OAuthUserInformation information){
        return new AuthResponse(null, null, true, preSignupToken, null, information);
    }

    public static AuthResponse ofRegisteredUser(Long userId, String nickName, JwtResponse token){
        return new AuthResponse(userId, nickName, false, null, token, null);
    }
}
