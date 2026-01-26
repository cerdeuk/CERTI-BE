package org.sopt.certi_server.domain.user.dto.response.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInformation(
        String sub,
        String name,
        @JsonProperty("given_name")
        String givenName,
        @JsonProperty("family_name")
        String familyName,
        String picture,
        String email,
        @JsonProperty("email_verified")
        boolean emailVerified,
        String locale
) {
}
