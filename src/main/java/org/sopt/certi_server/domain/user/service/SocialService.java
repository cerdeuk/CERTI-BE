package org.sopt.certi_server.domain.user.service;

import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;

public interface SocialService {
    public LoginUriResponse getAuthorizationUri();

    public OAuthUserInformation getUserInfo(String code);

    public OAuthUserInformation getUserInfoByAccessToken(String token);
}
