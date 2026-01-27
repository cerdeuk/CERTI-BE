package org.sopt.certi_server.domain.user.dto.response.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuthResponse(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("expires_in")
        Integer expiresIn,

        @JsonProperty("scope")
        String scope,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("id_token")
        String idToken,

        @JsonProperty("refresh_token")
        String refreshToken
) {
}
