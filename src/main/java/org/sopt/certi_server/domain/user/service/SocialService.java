package org.sopt.certi_server.domain.user.service;

import org.sopt.certi_server.domain.user.dto.response.LoginUriResponse;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public interface SocialService {
    public LoginUriResponse getAuthorizationUri();
}
