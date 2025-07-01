package org.sopt.certi_server.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
        boolean needSignUp,
        String preSignupToken,
        JwtResponse tokenResponse,
        OAuthUserInformation userInformation
) {
    public static AuthResponse ofNotRegisteredUser(String preSignupToken, OAuthUserInformation information){
        return new AuthResponse(true, preSignupToken, null, information);
    }

    public static AuthResponse ofRegisteredUser(JwtResponse token){
        return new AuthResponse(false, null, token, null);
    }
}
