package org.sopt.certi_server.domain.user.dto.request;

import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;

public record SignupRequest(
        OAuthUserInformation userInformation
) {
}
