package org.sopt.certi_server.domain.user.dto.request.google;

public record GoogleTokenRequest(
        String code,
        String client_id,
        String client_secret,
        String redirect_uri,
        String grant_type
) {
}
