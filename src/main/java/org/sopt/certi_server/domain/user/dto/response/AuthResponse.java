package org.sopt.certi_server.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
        Long userId,
        boolean needSignUp,
        String preSignupToken,
        JwtResponse tokenResponse,
        OAuthUserInformation userInformation
) {
    public static AuthResponse ofNotRegisteredUser(String preSignupToken, OAuthUserInformation information){
        return new AuthResponse(null, true, preSignupToken, null, information);
    }

    public static AuthResponse ofRegisteredUser(Long userId, JwtResponse token){
        return new AuthResponse(userId, false, null, token, null);
    }
}
